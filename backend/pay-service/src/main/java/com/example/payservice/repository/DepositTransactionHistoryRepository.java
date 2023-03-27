package com.example.payservice.repository;

import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepositTransactionHistoryRepository extends JpaRepository<DepositTransactionHistoryEntity, Long> {

    @Query("SELECT p FROM DepositTransactionHistoryEntity p where p.user = :payUserEntity and lower(p.type) like coalesce(:historyType, '%')")
    Page<DepositTransactionHistoryEntity> findAllByUserAndDepositTransactionType(
            PayUserEntity payUserEntity, String historyType, Pageable pageable);
}
