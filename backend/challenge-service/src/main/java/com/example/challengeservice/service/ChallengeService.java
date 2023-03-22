package com.example.challengeservice.service;

import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.dto.response.SimpleChallengeResponseDto;

import java.io.IOException;
import java.util.List;

public interface ChallengeService {
    /** 챌린지 생성 **/
    Long createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto) throws IOException;
    /** 챌린지 조회 **/
    ChallengeRoomResponseDto readChallenge(Long challengeId);
    /** 챌린지 참여 **/
    void joinChallenge(Long challengeId, Long userId);

    List<SimpleChallengeResponseDto> getListSimpleChallenge (String type , String search , int size , Long offset);
}
