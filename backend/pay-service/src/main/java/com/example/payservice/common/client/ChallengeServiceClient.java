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

import java.util.Map;

@FeignClient(name = "challenge-service")
public interface ChallengeServiceClient {
    @GetMapping("/challenges/{challengId}")
    InternalResponse<ChallengeInfo> getChallengeId(@PathVariable Long challengeId);

    @PostMapping("/challenges/listInfo")
    InternalResponse<Map<Long, SimpleChallengeInfo>> getSimpleChallengeInfos(
            @RequestBody SimpleChallengeInfosRequest request
    );
}
