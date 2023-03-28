package com.example.payservice.repository;

import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

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
}
