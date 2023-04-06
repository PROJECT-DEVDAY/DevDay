package com.example.payservice.repository;

import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.entity.DepositSummary;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface DepositTransactionHistoryRepository extends JpaRepository<DepositTransactionHistoryEntity, Long> {
    boolean existsDepositTransactionHistoryEntityByUserAndChallengeIdAndType(PayUserEntity entity, Long challengeId, DepositTransactionType type);
    Optional<DepositTransactionHistoryEntity> findByUserAndChallengeIdAndType(PayUserEntity entity, Long challengeId, DepositTransactionType type);
    @Query("SELECT p FROM DepositTransactionHistoryEntity p where p.user = :payUserEntity and lower(p.type) like coalesce(:historyType, '%')")
    Page<DepositTransactionHistoryEntity> findAllByUserAndDepositTransactionType(
            PayUserEntity payUserEntity, String historyType, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value="5000")})
    List<DepositTransactionHistoryEntity> findAllByChallengeIdAndType(Long challengeId, DepositTransactionType type);

    List<DepositTransactionHistoryEntity> findAllByUser(PayUserEntity entity);

    @Modifying
    @Query("DELETE FROM DepositTransactionHistoryEntity p where p.user = :user")
    void deleteAllByUser(PayUserEntity user);

    @Query("SELECT p.type as depositTransactionType, SUM(p.amount) as sum FROM DepositTransactionHistoryEntity p WHERE p.user = :user GROUP BY p.type")
    List<DepositSummary> getSummaryInfoByUserAndType(PayUserEntity user);

    @Query("SELECT p.challengeId " +
            "FROM DepositTransactionHistoryEntity p " +
            "WHERE p.user = :user and p.type in :types " +
            "GROUP BY p.challengeId, p.user " +
            "HAVING COUNT(*) >= 2")
    List<Long> getDoneChallengesByUser(PayUserEntity user, DepositTransactionType[] types);

    @Query("SELECT coalesce(SUM(p.amount), 0) " +
            "FROM DepositTransactionHistoryEntity p " +
            "WHERE p.user = :user and p.challengeId not in :challengeIds and p.type = :type"
    )
    Integer getCurrentChallengingAmountByUser(PayUserEntity user, List<Long> challengeIds, DepositTransactionType type);
}
