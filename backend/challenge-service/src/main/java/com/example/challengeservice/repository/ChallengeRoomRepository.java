package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ChallengeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRoomRepository extends JpaRepository<ChallengeRoom, Long> , ChallengeRoomRepoCustom {
    Optional<ChallengeRoom> findChallengeRoomById(Long id);

    @Query("select cr from ChallengeRoom cr where cr.startDate<=:getDate and cr.endDate >=:getDate")
    List<ChallengeRoom> findChallengingRoomByDate(@Param("getDate") String getDate);

    Long countByHostId(Long hostId);

    @Query("select cr.category from ChallengeRoom cr where cr.id = :challengeId")
    String getCategory (@Param("challengeId") Long challengeId);
}
