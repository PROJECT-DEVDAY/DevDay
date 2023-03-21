package com.example.challengeservice.repository;

import com.example.challengeservice.entity.UserChallenge;
import org.bouncycastle.asn1.cmp.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
    Optional<UserChallenge> findByChallengeIdAndUserId(Long challengeId, Long userId);

}
