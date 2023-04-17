package com.example.challengeservice.service.algo;

import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.response.ProgressResponseDto;
import com.example.challengeservice.dto.response.SolvedListResponseDto;
import com.example.challengeservice.dto.response.SolvedMapResponseDto;

public interface AlgoChallengeService {
    /** 푼 문제 리스트 찾기 **/
    SolvedListResponseDto solvedProblemList(String baekjoonId);
    /** 유저의 백준리스트 가져오기  **/
    void updateUserBaekjoon(Long userId);
    /** 해당 유저가 해당 날짜에 푼 알고리즘 리스트 조회 **/
    SolvedListResponseDto checkDateUserBaekjoon(Long userId, String selectDate);
    /** 해당 유저가 최근 5일 동안 푼 문제를 조회 **/
    SolvedMapResponseDto getRecentUserBaekjoon(Long userId);
    /** 알고리즘 인증 기록 생성하기 **/
    void createAlgoRecord(ChallengeRecordRequestDto requestDto);
    /** 나의 알고리즘 진행상황 보기 **/
    ProgressResponseDto getProgressUserBaekjoon(Long userId, Long challengeId);
}