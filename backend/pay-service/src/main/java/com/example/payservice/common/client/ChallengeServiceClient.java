package com.example.payservice.common.client;

import com.example.payservice.dto.response.ChallengeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "challenge-service")
public interface ChallengeServiceClient {
    @GetMapping("/challenge-service/challenges/{challengId}")
    ChallengeResponse getChallengeId(@PathVariable Long challengeId);
}
