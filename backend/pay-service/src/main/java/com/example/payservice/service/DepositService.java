package com.example.payservice.service;

import com.example.payservice.common.client.ChallengeServiceClient;
import com.example.payservice.dto.CustomPage;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.dto.deposit.DepositTransactionHistoryDto;
import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.dto.request.SimpleChallengeInfosRequest;
import com.example.payservice.dto.request.WithdrawDepositRequest;
import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.exception.LackOfDepositException;
import com.example.payservice.exception.UnRefundableException;
import com.example.payservice.repository.DepositTransactionHistoryRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.payservice.entity.DepositTransactionHistoryEntity.notHasChallengeFields;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {
    private final ChallengeServiceClient challengeServiceClient;
    private final UserService userService;
    private final PaymentService paymentService;
    private final DepositTransactionHistoryRepository depositTransactionHistoryRepository;
    /**
     * 예치금 내역을 조회하는 기능입니다.
     * @param userId
     * @param type
     * @param pageable
     * @return
     */
    public CustomPage<DepositTransactionHistoryDto> searchHistories(Long userId, String type, Pageable pageable) {
        PayUserEntity userEntity = userService.getPayUserEntity(userId);
        String historyType = String.valueOf(type).isEmpty() ? null : type;

        Page<DepositTransactionHistoryEntity> pages =
                depositTransactionHistoryRepository
                        .findAllByUserAndDepositTransactionType(
                                userEntity,
                                historyType,
                                pageable
                        );
        List<DepositTransactionHistoryDto> updatePages = getSyncChallengeList(pages.getContent());

        return CustomPage.<DepositTransactionHistoryDto>builder()
                .empty(pages.isEmpty())
                .first(pages.isFirst())
                .last(pages.isLast())
                .size(pages.getSize())
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .content(updatePages)
                .page(pages.getPageable().getPageNumber())
                .build();
    }

    /**
     * 검색된 예치금 내역을 challenge-service로부터 challenge의 정보를 업데이트 합니다.
     * @param challenges
     * @return
     */
    private List<DepositTransactionHistoryDto> getSyncChallengeList(List<DepositTransactionHistoryEntity> challenges) {
        List<DepositTransactionHistoryDto> syncChallenges = challenges.stream()
                .map(DepositTransactionHistoryDto::from)
                .collect(Collectors.toList());

        try {
            List<Long> challengeTypePayRefundIds = challenges.stream()
                    .filter(depositHistory -> !notHasChallengeFields(depositHistory.getType()))
                    .map(DepositTransactionHistoryEntity::getChallengeId)
                    .collect(Collectors.toList());

            if(!challengeTypePayRefundIds.isEmpty()) {
                SimpleChallengeInfosRequest request = SimpleChallengeInfosRequest.builder()
                        .challengeIdList(challengeTypePayRefundIds)
                        .build();

                Map<Long, SimpleChallengeInfo> challengeInfoMap = challengeServiceClient
                        .getSimpleChallengeInfos(request)
                        .getData();

                syncChallenges = challenges.stream().map(c -> {
                    DepositTransactionHistoryDto dto = DepositTransactionHistoryDto.from(c);
                    if(c.getType() != DepositTransactionType.CHARGE) {
                        dto.setChallenge(challengeInfoMap.getOrDefault(c.getChallengeId(), null));
                    }
                    return dto;
                }).collect(Collectors.toList());
            }
        } catch(FeignException ex) {
            log.error("challenge-service로부터 challenge 정보를 가져오는 데 실패했습니다. -> {}", ex.getMessage());
        } catch(Exception ex) {
            log.error("challenge-service의 데이터와 pay-service 데이터를 합치는 과정에 문제가 발생했습니다. -> {}", ex.getMessage());
        }

        return syncChallenges;
    }

    /**
     * 챌린지에 참가한 모든 사용자를 환불 시킵니다.
     *
     * @return
     */
    @Transactional
    public void refund(Long challengeId) {
        List<DepositTransactionHistoryEntity> paidUserHistoires = depositTransactionHistoryRepository.
                findAllByChallengeIdAndType(challengeId, DepositTransactionType.PAY);

        List<DepositTransactionHistoryEntity> refundUserHistories = depositTransactionHistoryRepository.
                findAllByChallengeIdAndType(challengeId, DepositTransactionType.REFUND);

        Map<Long, Boolean> alreadyRefundMap = makeAlreadyRefundUserMap(refundUserHistories);

        paidUserHistoires.stream()
            .filter(paidHistory ->
                checkRefunableUser(alreadyRefundMap, paidHistory.getUser().getUserId())
            )
            .forEach(historyEntity ->  refund(historyEntity.getUser(), historyEntity));
    }
    private boolean checkRefunableUser(Map<Long, Boolean> map, Long userId) {
        return !map.getOrDefault(userId, false);
    }
    private Map<Long, Boolean> makeAlreadyRefundUserMap(List<DepositTransactionHistoryEntity> refundedUserHistories) {
        Map<Long, Boolean> alreadyRefundMap = new HashMap<>();
        refundedUserHistories.forEach(refundUserHistory -> alreadyRefundMap.put(refundUserHistory.getUser().getUserId(), true));

        return alreadyRefundMap;
    }

    /**
     * 참가한 유저별로 챌린지에 대한 환불 기능입니다.
     * userId와 challengeId를 받습니다.
     * @param userId
     * @param challengeId
     * @return
     */
    @Transactional
    public void refund(Long userId, Long challengeId) {
        PayUserEntity userEntity = userService.getPayUserEntityForUpdate(userId);
        DepositTransactionHistoryEntity depositHistory = checkCanRefundUser(userEntity, challengeId);

        refund(userEntity, depositHistory);
    }
    @Transactional
    public void refund(PayUserEntity userEntity, DepositTransactionHistoryEntity depositHistory) {
        userEntity.updateDeposit(userEntity.getDeposit() + depositHistory.getAmount());
        depositTransactionHistoryRepository.save(DepositTransactionHistoryEntity
                .builder()
                .challengeId(depositHistory.getChallengeId())
                .user(userEntity)
                .type(DepositTransactionType.REFUND)
                .amount(depositHistory.getAmount())
                .depositTransaction(null)
                .build());
    }

    /**
     * 환불이 가능한 유저인지 확인합니다.
     * @param user
     * @param challengeId
     * @return
     */
    public DepositTransactionHistoryEntity checkCanRefundUser(PayUserEntity user, Long challengeId) {
        boolean hasRefundHistory = depositTransactionHistoryRepository
                .existsDepositTransactionHistoryEntityByUserAndChallengeIdAndType(user, challengeId, DepositTransactionType.REFUND);
        if(hasRefundHistory) {
            throw new UnRefundableException("환불 대상이 아닙니다.");
        }

        return depositTransactionHistoryRepository
                .findByUserAndChallengeIdAndType(user, challengeId, DepositTransactionType.PAY)
                .orElseThrow(() -> new UnRefundableException("환불 대상이 아닙니다."));

    }

    /**
     * 예치금 결제 취소 기능입니다.
     * @param userId
     * @param request
     * @return
     */
    @Transactional
    public WithdrawResponse withdraw(Long userId, WithdrawDepositRequest request) {
        PayUserEntity payUserEntity = userService.getPayUserEntityForUpdate(userId);
        checkWithdrawMoney(payUserEntity, request.getMoney());

        boolean result = paymentService.withdraw(payUserEntity, request.getMoney());
        return WithdrawResponse.builder()
            .result(result)
            .remainPrizes(payUserEntity.getPrize())
            .build();
    }


    /**
     * 유저가 money를 출금할 수 있는지 확인합니다.
     * @param payUserEntity
     * @param money
     * @throws LackOfDepositException
     */
    private void checkWithdrawMoney(PayUserEntity payUserEntity, int money) {
        if(payUserEntity.getDeposit() < money) {
            throw new LackOfDepositException("출금할 예치금 금액이 저장된 금액보다 큽니다.");
        }
    }
}
