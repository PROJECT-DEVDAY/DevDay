package com.example.userservice.client;

import com.example.userservice.dto.response.ChallengeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "challenge-service")
public interface ChallengeServiceClient {

    @GetMapping("/challenge-service/challenges/challengeInfo")
    ChallengeResponseDto getChallengeInfo(@PathVariable Long userId);
}
