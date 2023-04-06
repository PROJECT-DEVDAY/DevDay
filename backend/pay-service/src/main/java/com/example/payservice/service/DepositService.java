package com.example.payservice.service;

import com.example.payservice.client.ChallengeServiceClient;
import com.example.payservice.dto.CustomPage;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.dto.deposit.DepositSummaryDto;
import com.example.payservice.dto.deposit.DepositTransactionHistoryDto;
import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.dto.request.ChallengeSettleInfo;
import com.example.payservice.dto.request.SimpleChallengeInfosRequest;
import com.example.payservice.dto.request.WithdrawDepositRequest;
import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.entity.DepositSummary;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.exception.*;
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
import java.util.stream.Collectors;

import static com.example.payservice.entity.DepositTransactionHistoryEntity.hasChallengeFields;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {
    private final ChallengeServiceClient challengeServiceClient;
    private final UserService userService;
    private final PaymentService paymentService;
    private final PrizeService prizeService;
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
                    .filter(depositHistory -> hasChallengeFields(depositHistory.getType()))
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
            .remainPrizes(payUserEntity.getDeposit())
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
        if(money <= 0) {
            throw new BadRequestException("출금할 금액이 0원 이하일 수 없습니다.");
        }
    }

    /**
     * 챌린지가 종료된 뒤 정산을 위한 함수입니다.
     * @param challengeId
     * @param resultList
     */
    @Transactional
    public void settleChallenge(Long challengeId, List<ChallengeSettleInfo> resultList) {
        // 유저마다 챌린지에 결제한 정보를 DB로부터 조회합니다.
        resultList.forEach(result -> settle(challengeId, result));
    }

    @Transactional
    public void settleChallengeV2(List<ChallengeSettleInfo> resultList) {
        resultList.forEach(result -> settle(result.getChallengeId(), result));
    }

    /**
     * 유저마다 금액을 받아 환불금액보다 크다면 상금으로 입금, 환불금액보다 적다면 패널티를 적용합니다.
     * @param challengeId
     * @param result
     */
    @Transactional
    public void settle(long challengeId, ChallengeSettleInfo result) {
        try {
            PayUserEntity challengeUser = userService.getPayUserEntityForUpdate(result.getUserId());

            DepositTransactionHistoryEntity history = depositTransactionHistoryRepository.findByUserAndChallengeIdAndType(
                    challengeUser, challengeId, DepositTransactionType.PAY
            ).orElseThrow(() -> new UnRefundableException("결제 이력이 없는 유저입니다."));
            boolean isAlreadyRefundUser = depositTransactionHistoryRepository.existsDepositTransactionHistoryEntityByUserAndChallengeIdAndType(
                    challengeUser, challengeId, DepositTransactionType.REFUND
            );
            if(isAlreadyRefundUser) {
                throw new AlreadyRefundableException("이미 정산된 유저입니다.");
            }

            if(history.getAmount() < result.getMoney()) {
                // 결과 값이 결제 금액보다 크다면 상금 입금
                settleDeposit(challengeId, challengeUser, history.getAmount());
                prizeService.settlePrize(challengeId, challengeUser, result.getMoney() - history.getAmount());
            } else {
                // 환불금액 줄이기
                settleDeposit(challengeId, challengeUser, result.getMoney());
            }
        } catch(UserNotExistException ex) {
            log.error("pay-service에 userId: {}가 존재하지 않습니다.", result.getUserId());
        } catch(UnRefundableException ex) {
            log.error("challengeId: {}에 해당하는 userId: {}의 결제 이력이 없습니다.", challengeId, result.getUserId());
        } catch(AlreadyRefundableException ex) {
            log.error("challengeId: {}에 해당하는 userId: {}는 이미 정산되어 있습니다.", challengeId, result.getUserId());
        } catch(RuntimeException ex) {
            log.error("challengeId: {} 정산과정 중 문제가 발생했습니다. -> {}", challengeId, ex.getMessage());
        }
    }
    /**
     * 챌린지가 종료된 뒤, 정산과정에서 예치금이력을 등록합니다.
     * @param challengeId
     * @param challengeUser
     * @param amount
     */
    @Transactional
    public void settleDeposit(long challengeId, PayUserEntity challengeUser, int amount) {
        DepositTransactionHistoryEntity newRefundHistoryEntity = DepositTransactionHistoryEntity
                .builder()
                .user(challengeUser)
                .amount(amount)
                .challengeId(challengeId)
                .type(DepositTransactionType.REFUND)
                .build();
        challengeUser.setDeposit(challengeUser.getDeposit() + amount);
        depositTransactionHistoryRepository.save(newRefundHistoryEntity);
    }

    /**
     * 예치금에 대한 간단 정보(충전한 금액, 참가중인 금액, 환불한 금액) 조회합니다.
     *
     * 총 예치금 -> charge
     * 출금 -> cancel
     * 챌린지 참가중 -> (PAY, REFUND 묶음이 적용되지 않는) PAY
     * 챌린지 벌금 -> PAY - REFUND
     *
     * @param userId
     * @return
     */
    public DepositSummaryDto getSummary(Long userId) {
        PayUserEntity user = userService.getPayUserEntity(userId);
        List<DepositSummary> summaries = depositTransactionHistoryRepository.getSummaryInfoByUserAndType(user);

        int charge = 0;
        int cancel = 0;
        int pay = 0;
        int refund = 0;

        for(DepositSummary summary : summaries) {
            switch(summary.getDepositTransactionType()) {
                case CHARGE:
                    charge = summary.getSum();
                    break;
                case CANCEL:
                    cancel = summary.getSum();
                    break;
                case PAY:
                    pay = summary.getSum();
                    break;
                case REFUND:
                    refund = summary.getSum();
                    break;
            }
        }

        List<Long> doneChallengeIds = depositTransactionHistoryRepository.getDoneChallengesByUser(
                user,
                new DepositTransactionType[] {
                    DepositTransactionType.PAY, DepositTransactionType.REFUND
                }
        );

        int challenging = depositTransactionHistoryRepository.getCurrentChallengingAmountByUser(
                user,
                doneChallengeIds,
                DepositTransactionType.PAY
        );
        
        return DepositSummaryDto.builder()
                .charge(charge)
                .cancel(cancel)
                .penalty(pay - refund)
                .challenging(challenging)
                .build();
    }
}
