package com.example.userservice.client;

import com.example.userservice.dto.BaseResponseDto;
import com.example.userservice.dto.response.challenge.ChallengeResponseDto;
import com.example.userservice.dto.response.challenge.ProblemResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "challenge-service")
public interface ChallengeServiceClient {

    @GetMapping("/challenges/challengeInfo/users/{userId}")
    BaseResponseDto<ChallengeResponseDto> getChallengeInfo(@PathVariable Long userId);

    @GetMapping("/challenges/baekjoon/{baekjoonId}")
    BaseResponseDto<ProblemResponseDto> solvedProblemList(@PathVariable String baekjoonId);
}
