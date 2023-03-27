package com.example.payservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payservice.entity.DepositTransactionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositTransactionRepository extends JpaRepository<DepositTransactionEntity, Long> {

}
