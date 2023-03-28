package com.example.payservice.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payservice.entity.DepositTransactionEntity;
import com.example.payservice.entity.PayUserEntity;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositTransactionRepository extends JpaRepository<DepositTransactionEntity, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<DepositTransactionEntity> findAllByUserAndRefundableAmountIsGreaterThanOrderByCreatedAtAsc(
		PayUserEntity user,
		int fundableMoney
	);
}
