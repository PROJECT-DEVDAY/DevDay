package com.example.challengeservice.client;


import com.example.challengeservice.common.result.ListResult;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.CommitRequestDto;
import com.example.challengeservice.dto.request.ProblemRequestDto;
import com.example.challengeservice.dto.response.BaekjoonListResponseDto;
import com.example.challengeservice.dto.response.CommitResponseDto;
import com.example.challengeservice.dto.response.DateProblemResponseDto;
import com.example.challengeservice.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user/baekjoon/{userId}")
    SingleResult<BaekjoonListResponseDto> getUserBaekjoonList(@PathVariable Long userId);

    @PostMapping("/user/baekjoon/{userId}")
    SingleResult<BaekjoonListResponseDto> createProblem(@PathVariable Long userId, @RequestBody ProblemRequestDto requestDto);

    @GetMapping("/user/{userId}")
    SingleResult<UserResponseDto> getUserInfo(@PathVariable Long userId);

    @GetMapping("/user/baekjoon/date/{userId}")
    ListResult<DateProblemResponseDto> getDateBaekjoonList(@PathVariable Long userId,
                                                           @RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate);
    @GetMapping("/user/commit/{userId}/{commitDate}")
    SingleResult<CommitResponseDto> getCommitRecord(@PathVariable("userId") Long userId,
                                                                              @PathVariable("commitDate") String commitDate);
    @PostMapping("/user/commit/{userId}")
    SingleResult<?> updateCommitCount(@PathVariable Long userId,
                                                                @RequestBody CommitRequestDto requestDto);
}