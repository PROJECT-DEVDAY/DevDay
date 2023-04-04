package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord,Long> ,ChallengeRecordRepoCustom{



   Optional<ChallengeRecord> findById( Long challengeRecordId);

   boolean existsById( Long challengeRecordId);

//   List<ChallengeRecord> findAllByUserChallengeId(Long userChallengeId);

   @Query("select cr from ChallengeRecord cr where cr.userChallenge.id=:userChallengeId and cr.createAt between :startDate and :endDate and cr.success=:success and cr.algorithmCount >=:algorithmCount")
   List<ChallengeRecord> findAllByUserChallengeIdAndStartDateAndEndDateAlgo(Long userChallengeId, String startDate, String endDate, boolean success, int algorithmCount);


}
