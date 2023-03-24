package com.example.challengeservice.repository;

import com.example.challengeservice.entity.UserChallenge;
import org.apache.catalina.User;
import org.bouncycastle.asn1.cmp.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
    Optional<UserChallenge> findByChallengeRoomIdAndUserId(Long challengeId, Long userId);
//    List<UserChallenge> findAllByChallengeId(Long challengeId);

    Optional<UserChallenge> findById(Long id);
    int countByChallengeRoomId(Long challengeId);
    List<UserChallenge> findAllByUserId(Long userId);

    /**
     * 해당 유저가 현재 진행중인 챌린지들을 반환하는 쿼리
     * @param userId
     * @return
     */
    @Query("select uc from UserChallenge uc where uc.userId=:userId and uc.challengeRoom.endDate >=:getDate")
    List<UserChallenge> findUserChallengingByUserId(@Param("userId") Long userId, @Param("getDate") String getDate);

    /**
     * 해당 유저가 완료한 챌린지들을 반환하는 쿼리
     * @param userId
     * @return
     */
    @Query("select uc from UserChallenge uc where uc.userId=:userId and uc.challengeRoom.endDate <:getDate")
    List<UserChallenge> findUserChallengedByUserId(@Param("userId") Long userId, @Param("getDate") String getDate);

    /**
     * 해당 유저가 방장인 챌린지들 조회
     * @param userId
     * @return
     */
    @Query("select uc from UserChallenge uc where uc.userId=:userId and uc.challengeRoom.hostId =:userId")
    List<UserChallenge> findUserHostChallengesUserId(@Param("userId") Long userId);


}