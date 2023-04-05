package com.example.payservice.client;

import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.challenge.ChallengeInfo;
import com.example.payservice.dto.challenge.ChallengeJoinRequestDto;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.dto.request.SimpleChallengeInfosRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "challenge-service")
public interface ChallengeServiceClient {
    @GetMapping("/challenges/{challengId}")
    InternalResponse<ChallengeInfo> getChallengeId(@PathVariable Long challengeId);

    @PostMapping("/challenges/listInfo")
    InternalResponse<Map<Long, SimpleChallengeInfo>> getSimpleChallengeInfos(
            @RequestBody SimpleChallengeInfosRequest request
    );
    @PostMapping("/auth/challenges/join")
    void sendApproveUserJoinChallenge(@RequestHeader("userId") Long userId, @RequestBody ChallengeJoinRequestDto requestDto);
}
