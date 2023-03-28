package com.example.challengeservice.client;


import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.ProblemRequestDto;
import com.example.challengeservice.dto.response.BaekjoonListResponseDto;
import com.example.challengeservice.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user/baekjoon/{userId}")
    SingleResult<BaekjoonListResponseDto> getUserBaekjoonList(@PathVariable Long userId);

    @PostMapping("/user/baekjoon/{userId}")
    SingleResult<BaekjoonListResponseDto> createProblem(@PathVariable Long userId, @RequestBody ProblemRequestDto requestDto);

    @GetMapping("/user/{userId}")
    SingleResult<UserResponseDto> getUserInfo(@PathVariable Long userId);
}
