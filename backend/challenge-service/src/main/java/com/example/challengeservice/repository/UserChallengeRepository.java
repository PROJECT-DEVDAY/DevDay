package com.example.challengeservice.repository;

import com.example.challengeservice.entity.UserChallenge;
import org.apache.catalina.User;
import org.bouncycastle.asn1.cmp.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
    Optional<UserChallenge> findByChallengeRoomIdAndUserId(Long challengeId, Long userId);
//    List<UserChallenge> findAllByChallengeId(Long challengeId);

    Optional<UserChallenge> findById(Long id);
    int countByChallengeRoomId(Long challengeId);





}
