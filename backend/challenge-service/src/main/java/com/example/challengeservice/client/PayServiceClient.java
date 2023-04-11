package com.example.challengeservice.client;

import com.example.challengeservice.client.dto.ChallengeSettlementRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "pay-service")
public interface PayServiceClient {

    /** 챌린지 종료 후 해당 유저의 정산 내역을 pay-service에 저장합니다**/
    @PostMapping("/challenges/settle")
    ResponseEntity<Void> requestChallengeSettlement (@RequestBody ChallengeSettlementRequest settlementDtoList);
}
