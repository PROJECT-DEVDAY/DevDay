package com.example.challengeservice.controller;

import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.Result;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.response.CommitCountResponseDto;
import com.example.challengeservice.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class CommitController {
    private final ResponseService responseService;

    private final ChallengeService challengeService;

    /**
     * 신대득
     * 유저 깃허브 아이디를 통해 해당 유저의 커밋 찾기 (크롤링)
     * 나온 결과를 계산해서 user에 넣어줘야한다.
     */
    @GetMapping("/github/{githubId}")
    public SingleResult<CommitCountResponseDto> getGithubList(@PathVariable("githubId") String githubId){
        return responseService.getSingleResult(challengeService.getGithubCommit(githubId));
    }

    /**
     * 신대득
     * 유저의 커밋 정보 갱신
     */
    @PostMapping("/github/update/users")
    public Result updateUserBaekjoon(HttpServletRequest request){
        Long userId=Long.parseLong(request.getHeader("userId"));
        challengeService.updateUserCommit(userId);
        return responseService.getSuccessResult();
    }



}
