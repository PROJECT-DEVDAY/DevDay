package com.example.challengeservice.service;

import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
<<<<<<< Updated upstream
import com.example.challengeservice.dto.response.SimpleChallengeResponseDto;
=======
import com.example.challengeservice.dto.response.SolvedListResponseDto;
>>>>>>> Stashed changes

import java.io.IOException;
import java.util.List;

public interface ChallengeService {
    /** 챌린지 생성 **/
    Long createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto) throws IOException;
    /** 챌린지 조회 **/
    ChallengeRoomResponseDto readChallenge(Long challengeId);
    /** 챌린지 참여 **/
<<<<<<< Updated upstream
    void joinChallenge(Long challengeId, Long userId);

    List<SimpleChallengeResponseDto> getListSimpleChallenge (String type , String search , int size , Long offset);
=======
    Long joinChallenge(Long challengeId, Long userId);
    /** 푼 문제 리스트 찾기 **/
    SolvedListResponseDto solvedProblemList(String baekjoonId);
>>>>>>> Stashed changes
}
