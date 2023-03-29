package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord,Long> ,ChallengeRecordRepoCustom{

/*    Optional<ChallengeRecord> findByUserChallengeId (Long userChallengeId);

    Optional<ChallengeRecord> findChallengeRecordByUserChallengeChallengeRoomId (Long challengeRoomId);*/

    Optional<ChallengeRecord> findById(Long challengeRecordId);



}
