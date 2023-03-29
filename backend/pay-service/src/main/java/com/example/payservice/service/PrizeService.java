package com.example.payservice.service;

import com.example.payservice.common.client.ChallengeServiceClient;
import com.example.payservice.dto.CustomPage;
import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.dto.prize.PrizeHistoryDto;
import com.example.payservice.dto.prize.PrizeHistoryType;
import com.example.payservice.dto.request.RewardSaveRequest;
import com.example.payservice.dto.request.SimpleChallengeInfosRequest;
import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.entity.PrizeHistoryEntity;
import com.example.payservice.exception.LackOfPrizeException;
import com.example.payservice.repository.PrizeHistoryRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrizeService {
    private final ChallengeServiceClient challengeServiceClient;
    private final PrizeHistoryRepository prizeHistoryRepository;
    private final PaymentService paymentService;
    private final UserService userService;

    /**
     * 상금을 환급하고 히스토리에 이력을 남깁니다.
     * @param userId
     * @param money
     * @param account
     * @return
     * @throws Exception
     */
    @Transactional
    public WithdrawResponse withdraw(long userId, int money, AccountDto account) {

        // 유저의 출금가능금액과 요청 금액을 비교해본다.
        PayUserEntity payUserEntity = userService.getPayUserEntityForUpdate(userId);
        checkDrawMoney(payUserEntity, money);
        /*
            TODO: [우선순위 낮음] 요청한 계좌가 유효한지 확인한다.
            계좌 유효 검사 시에 주민등록 앞자리가 필요함
        */

        // 출금을 반영합니다.
        boolean result = paymentService.transferMoney(account, money);
        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.createOutTypePrizeHistory(account, money);

        // transaction 반영
        prizeHistory.setUser(payUserEntity);
        prizeHistoryRepository.save(prizeHistory);

        return WithdrawResponse.builder()
            .result(result)
            .remainPrizes(payUserEntity.getPrize())
            .build();
    }

    /**
     * 상금 획득 이력을 남깁니다.
     * @param request
     */
    @Transactional
    public void save(RewardSaveRequest request) {
        PayUserEntity payUserEntity = userService.getPayUserEntityForUpdate(request.getUserId());
        // TODO: 챌린지 ID가 존재하는 지
        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.createInTypePrizeHistory(request);

        // transaction 반영
        prizeHistory.setUser(payUserEntity);
        prizeHistoryRepository.save(prizeHistory);
    }

    /**
     * 유저의 환급/획득(in/out) 이력을 Page로 얻습니다.
     * @param userId
     * @param type
     * @param pageable
     * @return
     */
    public CustomPage<PrizeHistoryDto> searchHistories(long userId, String type, Pageable pageable) {
        PayUserEntity payUserEntity = userService.getPayUserEntity(userId);
        String historyType = String.valueOf(type).isEmpty() ? null : type;

        Page<PrizeHistoryEntity> pages = prizeHistoryRepository
                .findAllByUserAndPrizeHistoryType(payUserEntity, historyType, pageable);
        List<PrizeHistoryDto> updatedPages = getSyncChallengeLists(pages.getContent());


        return CustomPage.<PrizeHistoryDto>builder()
                .empty(pages.isEmpty())
                .first(pages.isFirst())
                .last(pages.isLast())
                .size(pages.getSize())
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .content(updatedPages)
                .page(pages.getPageable().getPageNumber())
                .build();
    }

    /**
     * challenge-service에서 challenge 정보를 조회해 업데이트 후 리스트를 반환합니다.
     * @param challenges
     * @return
     */
    private List<PrizeHistoryDto> getSyncChallengeLists(List<PrizeHistoryEntity> challenges) {
        log.info("{}", challenges);
        List<PrizeHistoryDto> syncChallenges = challenges.stream()
                .map(PrizeHistoryDto::from).collect(Collectors.toList());

        try {
            List<Long> challengeTypeInIds = challenges.stream()
                    .filter(prizeHistory -> prizeHistory.getPrizeHistoryType() == PrizeHistoryType.IN)
                    .map(PrizeHistoryEntity::getChallengeId)
                    .collect(Collectors.toList());

            if(!challengeTypeInIds.isEmpty()) {
                SimpleChallengeInfosRequest request = SimpleChallengeInfosRequest.builder()
                        .challengeIdList(challengeTypeInIds)
                        .build();

                Map<Long, SimpleChallengeInfo> challengeInfoMap = challengeServiceClient
                    .getSimpleChallengeInfos(request)
                    .getData();

                syncChallenges = challenges.stream().map(c -> {
                    PrizeHistoryDto dto = PrizeHistoryDto.from(c);
                    // challenge 정보를 업데이트 합니다.
                    if (c.getPrizeHistoryType() == PrizeHistoryType.IN)
                        dto.setChallenge(challengeInfoMap.getOrDefault(c.getChallengeId(), null));
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
     * 유저가 money를 출금할 수 있는지 확인합니다.
     * @param payUserEntity
     * @param money
     * @throws Exception
     */
    private void checkDrawMoney(PayUserEntity payUserEntity, int money) {
        if(payUserEntity.getPrize() < money) {
            throw new LackOfPrizeException("출금할 상금 금액이 저장된 금액보다 큽니다.");
        }
    }

    /**
     * 챌린지가 종료된 뒤, 정산과정에서 상금이력을 등록합니다.
     * @param challengeId
     * @param challengeUser
     * @param amount
     */
    @Transactional
    public void settlePrize(long challengeId, PayUserEntity challengeUser, int amount) {
        PrizeHistoryEntity newPrizeHistoryEntity = PrizeHistoryEntity.builder()
                .prizeHistoryType(PrizeHistoryType.IN)
                .challengeId(challengeId)
                .user(challengeUser)
                .amount(amount)
                .id(String.valueOf(UUID.randomUUID()))
                .build();
        challengeUser.setPrize(challengeUser.getPrize() + amount);
        prizeHistoryRepository.save(newPrizeHistoryEntity);
    }
}
