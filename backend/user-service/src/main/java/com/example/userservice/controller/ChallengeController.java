package com.example.userservice.controller;

import com.example.userservice.dto.BaseResponseDto;
import com.example.userservice.dto.request.ProblemRequestDto;
import com.example.userservice.dto.response.BaekjoonListResponseDto;
import com.example.userservice.dto.response.DateProblemResponseDto;
import com.example.userservice.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ChallengeController {

    private final ChallengeService challengeService;

    /**
     * 챌린지 마이크로 서비스에서 백준 문제 리스트를 FeignClient 로 가져오기 위한 API 입니다.
     * @param userId
     * @return
     * */
    @GetMapping("/user/baekjoon/{userId}")
    public ResponseEntity<BaseResponseDto<BaekjoonListResponseDto>> getBaekjoonList(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", challengeService.getBaekjoonList(userId)));
    }

    /**
     * 챌린지 마이크로 서비스에서 FeignClient 로 오늘 푼 문제 리스트를 저장하기 위한 API 입니다.
     * @param userId
     * @param requestDto
     * */
    @PostMapping("/user/baekjoon/{userId}")
    public ResponseEntity<BaseResponseDto<?>> createProblem(@PathVariable Long userId,
                                                            @RequestBody ProblemRequestDto requestDto) {
        challengeService.createProblem(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDto<>(201, "success"));
    }

    /**
     * 챌린지 마이크로 서비스에서 FeignClient 로 특정 날짜 사이에 푼 문제 리스트를 조회하기 위한 API 입니다.
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     * */
    @GetMapping("/user/baekjoon/date/{userId}")
    public ResponseEntity<BaseResponseDto<List<DateProblemResponseDto>>> getDateBaekjoonList(@PathVariable Long userId,
                                                                                             @RequestParam("startDate") String startDate,
                                                                                             @RequestParam("endDate") String endDate) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", challengeService.getDateBaekjoonList(userId, startDate, endDate)));
    }
}
