package com.example.payservice.repository;

import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface DepositTransactionHistoryRepository extends JpaRepository<DepositTransactionHistoryEntity, Long> {
    boolean existsDepositTransactionHistoryEntityByUserAndChallengeIdAndType(PayUserEntity entity, Long challengeId, DepositTransactionType type);
    Optional<DepositTransactionHistoryEntity> findByUserAndChallengeIdAndType(PayUserEntity entity, Long challengeId, DepositTransactionType type);
    @Query("SELECT p FROM DepositTransactionHistoryEntity p where p.user = :payUserEntity and lower(p.type) like coalesce(:historyType, '%')")
    Page<DepositTransactionHistoryEntity> findAllByUserAndDepositTransactionType(
            PayUserEntity payUserEntity, String historyType, Pageable pageable);

    Set<DepositTransactionHistoryEntity> findAllByChallengeIdAndType(Long challengeId, DepositTransactionType type);
}
