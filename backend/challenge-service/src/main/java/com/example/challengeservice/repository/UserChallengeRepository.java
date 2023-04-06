package com.example.challengeservice.repository;

import com.example.challengeservice.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
    Optional<UserChallenge> findByChallengeRoomIdAndUserId(Long challengeRoomId, Long userId);

    List<UserChallenge> findAllByChallengeRoomId(Long challengeRoomId);
    Optional<UserChallenge> findById(Long id);

    /** 해당 날짜에 그 카테고리로 만들어진 UserChallenge 리스트 가져오기 **/
    @Query("select uc from UserChallenge uc join ChallengeRoom cr on uc.challengeRoom.id=cr.id where uc.challengeRoom.startDate<=:getDate and uc.challengeRoom.endDate >=:getDate and uc.challengeRoom.category =:category")
    List<UserChallenge> findAllByDateAndCategory(@Param("getDate") String getDate, @Param("category") String category);

    int countByChallengeRoomId(Long challengeId);
    List<UserChallenge> findAllByUserId(Long userId);

    /**
     * 해당 유저가 현재 진행중인 챌린지들을 반환하는 쿼리
     * @param userId
     * @return
     */
    @Query("select uc from UserChallenge uc where uc.userId=:userId and uc.challengeRoom.endDate >=:getDate and uc.challengeRoom.startDate <=:getDate")
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

    @Query("select uc from UserChallenge uc where uc.challengeRoom.id=:challengeId")
    List<UserChallenge> findUserChallengesByChallengeRoomId(@Param("challengeId") Long challengeId);

    boolean existsByChallengeRoomIdAndUserId(Long challengeRoomId , Long userId);



}
