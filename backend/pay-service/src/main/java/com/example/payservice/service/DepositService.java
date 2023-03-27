package com.example.payservice.service;

import com.example.payservice.common.client.ChallengeServiceClient;
import com.example.payservice.dto.CustomPage;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.dto.deposit.DepositTransactionHistoryDto;
import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.dto.request.SimpleChallengeInfosRequest;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.repository.DepositTransactionHistoryRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositService {
    private final ChallengeServiceClient challengeServiceClient;
    private final UserService userService;
    private final DepositTransactionHistoryRepository depositTransactionHistoryRepository;
    
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

    private List<DepositTransactionHistoryDto> getSyncChallengeList(List<DepositTransactionHistoryEntity> challenges) {
        List<DepositTransactionHistoryDto> syncChallenges = challenges.stream()
                .map(DepositTransactionHistoryDto::from)
                .collect(Collectors.toList());

        try {
            List<Long> challengeTypePayRefundIds = challenges.stream()
                    .filter(depositHistory -> depositHistory.getType() != DepositTransactionType.CHARGE)
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
}
