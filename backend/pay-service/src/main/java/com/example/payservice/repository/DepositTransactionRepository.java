package com.example.payservice.repository;

import com.example.payservice.entity.DepositTransactionEntity;
import com.example.payservice.entity.PayUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface DepositTransactionRepository extends JpaRepository<DepositTransactionEntity, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<DepositTransactionEntity> findAllByUserAndRefundableAmountIsGreaterThanOrderByCreatedAtAsc(
		PayUserEntity user,
		int fundableMoney
	);

	List<DepositTransactionEntity> findAllByUser(PayUserEntity entity);
	@Modifying
	@Query("DELETE FROM DepositTransactionEntity p where p.user = :user")
	void deleteAllByUser(PayUserEntity user);
}
