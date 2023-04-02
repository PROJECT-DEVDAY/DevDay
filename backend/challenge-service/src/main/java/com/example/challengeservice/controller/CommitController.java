package com.example.challengeservice.controller;

import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.response.CommitCountResponseDto;
import com.example.challengeservice.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Todo : 반환 타입 등 다시 만들 예정
     * Todo : userId로 commit 가져오는걸로 바꾸기
     */
    @GetMapping("/github/{githubId}")
    public SingleResult<CommitCountResponseDto> getGithubList(@PathVariable("githubId") String githubId){
        return responseService.getSingleResult(challengeService.getGithubCommit(githubId));
    }

}
