package com.example.payservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payservice.entity.DepositTransactionHistoryEntity;

public interface DepositTransactionHistoryRepository extends JpaRepository<DepositTransactionHistoryEntity, Long> {

}
