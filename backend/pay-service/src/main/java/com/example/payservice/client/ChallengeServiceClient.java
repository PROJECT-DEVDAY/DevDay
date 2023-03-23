package com.example.payservice.client;

import com.example.payservice.dto.ChallengeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "challenge-service")
public interface ChallengeServiceClient {
    @GetMapping("/challenge-service/challenges/{challengId}")
    ChallengeDto getChallengeId(@PathVariable Long challengeId);
}
