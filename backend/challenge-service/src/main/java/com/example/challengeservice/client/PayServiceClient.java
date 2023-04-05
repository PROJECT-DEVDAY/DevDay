package com.example.challengeservice.client;

import com.example.challengeservice.client.dto.ChallengeSettlementRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "pay-service")
public interface PayServiceClient {

    @PostMapping("/challenges/settle")
    ResponseEntity<Void> requestChallengeSettlement (@RequestBody ChallengeSettlementRequest settlementDtoList);
}
