package com.example.challengeservice.service.commit;

import com.example.challengeservice.client.dto.CommitResponseDto;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.response.CommitCountResponseDto;
import com.example.challengeservice.dto.response.SolvedMapResponseDto;

public interface CommitChallengeService {
    /** 깃허브 커밋 리스트 찾기 **/
    CommitCountResponseDto getGithubCommit(String githubId);
    /** 유저의 커밋리스트 업데이트 **/
    void updateUserCommit(Long userId);
    /** 해당 유저가 해당 날짜에 커밋한 개수를 조회 **/
    CommitResponseDto checkDateUserCommit(Long userId, String selectDate);
    /** 해당 유저가 최근 5일 동안의 커밋 개수 조회 **/
    SolvedMapResponseDto getRecentUserCommit(Long userId);
    /** 커밋 인증 기록을 저장 **/
    void createCommitRecord(ChallengeRecordRequestDto requestDto);
}
