package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ChallengeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface ChallengeRoomRepository extends JpaRepository<ChallengeRoom, Long> {
}
