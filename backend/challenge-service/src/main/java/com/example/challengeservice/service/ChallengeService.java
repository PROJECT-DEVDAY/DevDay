package com.example.challengeservice.service;

import com.example.challengeservice.dto.request.ChallengeJoinRequestDto;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.request.ReportRecordRequestDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.entity.ChallengeRoom;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ChallengeService {



    /** 깃허브 커밋 리스트 찾기 **/
    CommitCountResponseDto getGithubCommit(String githubId);
    /** 푼 문제 리스트 찾기 **/
    SolvedListResponseDto solvedProblemList(String baekjoonId);
    /** 유저의 백준리스트 가져오기  **/
    void updateUserBaekjoon(Long userId);
    /** 방의 모든 유저의 백준 리스트 업데이트 **/
    void updateChallengeRoom(Long challengeRoomId);
    /** 해당 유저가 해당 날짜에 푼 알고리즘 리스트 조회 **/
    SolvedListResponseDto checkDateUserBaekjoon(Long userId, String selectDate);
    CommitResponseDto checkDateUserCommit(Long userId, String selectDate);
    SolvedMapResponseDto getRecentUserBaekjoon(Long userId);
    SolvedMapResponseDto getRecentUserCommit(Long userId);

    /** 스케줄러 저장 메서드 **/
    void createDailyRecord();
    /** 알고리즘 인증 기록 생성하기 **/
    void createAlgoRecord(ChallengeRecordRequestDto requestDto) throws IOException;



    void oneDayCulc(ChallengeRoom challengeRoom);
    void culcDailyPayment();
    void updateUserCommit(Long userId);
    void createCommitRecord(ChallengeRecordRequestDto requestDto);
    /** 나의 현재 진행상황 보기 **/
    ProgressResponseDto getProgressUserBaekjoon(Long userId, Long challengeId);

    List<RankResponseDto> getTopRank(Long challengeId);
    /** 유저가 선택한 챌린지에 대한 증명서 정보를 반환하는 메서드 **/
    CertificationResponseDto getCertification(Long userId, Long challengeRoomId);
}
