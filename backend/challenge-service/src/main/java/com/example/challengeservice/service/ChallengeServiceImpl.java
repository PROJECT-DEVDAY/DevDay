package com.example.challengeservice.service;

import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService{
    private final UserChallengeRepository userChallengeRepository;
    @Override
    @Transactional
    public void joinChallenge(Long challengeId, Long userId) {
        // userId 해당 challengeId에 이미 있는지 확인
        Optional<UserChallenge> checkUserChallenge=userChallengeRepository.findByChallengeIdAndUserId(challengeId, userId);
        if (checkUserChallenge.isPresent()) throw new ApiException(ExceptionEnum.USER_CHALLENGE_EXIST_EXCEPTION);

        // 없다면, 생성
        UserChallenge userChallenge=UserChallenge.from(challengeId, userId);
        userChallengeRepository.save(userChallenge);
    }

}
