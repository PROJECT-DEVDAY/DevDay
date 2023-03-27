package com.example.payservice.service;

import com.example.payservice.common.client.ChallengeServiceClient;
import com.example.payservice.dto.CustomPage;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.dto.deposit.DepositTransactionHistoryDto;
import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.dto.request.SimpleChallengeInfosRequest;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.exception.UnRefundableException;
import com.example.payservice.repository.DepositTransactionHistoryRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.payservice.entity.DepositTransactionHistoryEntity.notHasChallengeFields;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositService {
    private final ChallengeServiceClient challengeServiceClient;
    private final UserService userService;
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
                    .map(depositHistory -> depositHistory.getChallengeId())
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
        // TODO: 성능이 안좋을 것으로 보여 재개발 필요
        Set<DepositTransactionHistoryEntity> paidSets = depositTransactionHistoryRepository.
                findAllByChallengeIdAndType(challengeId, DepositTransactionType.PAY);
        Set<DepositTransactionHistoryEntity> refundSets = depositTransactionHistoryRepository.
                findAllByChallengeIdAndType(challengeId, DepositTransactionType.REFUND);

        paidSets.removeAll(refundSets);
        paidSets.forEach(historyEntity -> {
            refund(historyEntity.getUser(), historyEntity);
        });
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

        DepositTransactionHistoryEntity payHistory = depositTransactionHistoryRepository
                .findByUserAndChallengeIdAndType(user, challengeId, DepositTransactionType.PAY)
                .orElseThrow(() -> new UnRefundableException("환불 대상이 아닙니다."));

        return payHistory;
    }
}
