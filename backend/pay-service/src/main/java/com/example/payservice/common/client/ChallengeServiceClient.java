package com.example.payservice.common.client;

import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.challenge.ChallengeInfo;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.dto.request.SimpleChallengeInfosRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "challenge-service")
public interface ChallengeServiceClient {
    @GetMapping("/challenge-service/challenges/{challengId}")
    InternalResponse<ChallengeInfo> getChallengeId(@PathVariable Long challengeId);

    @PostMapping("/challenge-service/challenges/listInfo")
    default InternalResponse<Map<Long, SimpleChallengeInfo>> getSimpleChallengeInfos(
            @RequestBody SimpleChallengeInfosRequest request
    ) {
        Map<Long, SimpleChallengeInfo> challengeInfoMap = new HashMap<>();
        request.getChallengeIdList().forEach(id -> {
            challengeInfoMap.put(id, new SimpleChallengeInfo(id, String.format("챌린지 테스트 ID: %d", id), "2020-12-12", "2020-12-24"));
        });
        return new InternalResponse<>(challengeInfoMap);
    }
}
