package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ChallengeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeRoomRepository extends JpaRepository<ChallengeRoom, Long> , ChallengeRoomRepoCustom {
    Optional<ChallengeRoom> findChallengeRoomById(Long id) ;
}
