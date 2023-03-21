package com.example.challengeservice.service;

import com.example.challengeservice.dto.request.ChallengeRequestDto;

import java.io.IOException;

public interface ChallengeService {
    /** 챌린지 생성 **/
    void createChallenge(ChallengeRequestDto challengeRequestDto) throws IOException;
     void joinChallenge(Long challengeId, Long userId);
}
