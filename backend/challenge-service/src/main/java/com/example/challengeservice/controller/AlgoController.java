package com.example.challengeservice.controller;

import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.Result;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.response.SolvedListResponseDto;
import com.example.challengeservice.service.algo.AlgoChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class AlgoController {
    private ResponseService responseService;
    private AlgoChallengeService algoChallengeService;

    /**
     * 신대득
     * 유저 백준 아이디를 통해 해당 유저의 푼 문제 리스트 찾기 (크롤링)
     * 나온 결과를 계산해서 user에 넣어줘야한다.
     */
    @GetMapping("/baekjoon/{baekjoonId}")
    public SingleResult<SolvedListResponseDto> solvedProblemList(@PathVariable("baekjoonId") String baekjoonId){
        return responseService.getSingleResult(algoChallengeService.solvedProblemList(baekjoonId));
    }

    /**
     * 신대득
     * 선택한 유저가
     * 해당 날짜에 푼 문제를 조회하는 API
     * @param userId // 조회 할 유저의 id
     * @param selectDate // 조회 할 날짜
     * @return
     */
    @GetMapping("/baekjoon/users/date")
    public SingleResult<SolvedListResponseDto> checkDateUserBaekjoon(@RequestParam Long userId, @RequestParam String selectDate){
        return responseService.getSingleResult(algoChallengeService.checkDateUserBaekjoon(userId, selectDate));
    }

    /**
     * 신대득
     * 유저가 푼 문제 리스트 갱신
     */
    @PostMapping("/baekjoon/update/users/{userId}")
    public Result updateUserBaekjoon(@PathVariable String userId){
        algoChallengeService.updateUserBaekjoon(Long.parseLong(userId));
        return responseService.getSuccessResult();
    }

}
