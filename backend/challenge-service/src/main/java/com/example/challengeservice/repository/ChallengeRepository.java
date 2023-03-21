package com.example.challengeservice.repository;

import org.bouncycastle.asn1.cmp.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
}
