package com.example.challengeservice.service;

import com.example.challengeservice.client.PayServiceClient;
import com.example.challengeservice.client.dto.ChallengeSettlementRequest;
import com.example.challengeservice.repository.ChallengeRoomRepository;
import com.example.challengeservice.service.common.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService{

    private final ChallengeRoomRepository challengeRoomRepository;

    private final PayServiceClient payServiceClient;

    private final CommonService commonService;
    @Override
    public void endChallengeCalculate() {


      payServiceClient.requestChallengeSettlement(new ChallengeSettlementRequest(challengeRoomRepository.findClosedChallengeUser(commonService.getPastDay(0,commonService.getDate()))));
    }
}
