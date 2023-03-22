package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ChallengeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRoomRepository extends JpaRepository<ChallengeRoom, Long> {
    Optional<ChallengeRoom> findChallengeRoomById(Long id) ;
}
