package com.example.payservice.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.payservice.dto.deposit.DepositTransactionType;

@DataJpaTest
@ActiveProfiles("test")
public class DepositTransactionHistoryEntityTest {

	@Test
	@DisplayName("챌린지 필드가 없는 엔티티 반환하기")
	void 챌린지_필드가_없는_엔티티들_반환() {
		DepositTransactionHistoryEntity payEntity = DepositTransactionHistoryEntity.builder()
			.id(1L)
			.type(DepositTransactionType.PAY)
			.build();
		DepositTransactionHistoryEntity cancelEntity = DepositTransactionHistoryEntity.builder()
			.id(2L)
			.type(DepositTransactionType.CANCEL)
			.build();
		DepositTransactionHistoryEntity refundEntity = DepositTransactionHistoryEntity.builder()
			.id(3L)
			.type(DepositTransactionType.REFUND)
			.build();
		DepositTransactionHistoryEntity chargeEntity = DepositTransactionHistoryEntity.builder()
			.id(4L)
			.type(DepositTransactionType.CHARGE)
			.build();
		Assertions.assertTrue(DepositTransactionHistoryEntity.notHasChallengeFields(payEntity.getType()));
		Assertions.assertTrue(DepositTransactionHistoryEntity.notHasChallengeFields(cancelEntity.getType()));
		Assertions.assertFalse(DepositTransactionHistoryEntity.notHasChallengeFields(chargeEntity.getType()));
		Assertions.assertFalse(DepositTransactionHistoryEntity.notHasChallengeFields(refundEntity.getType()));
	}

	@Test
	@DisplayName("히스토리 엔티티 equal 체크하기")
	void historyEntityEqualCheck() {
		DepositTransactionHistoryEntity refundEntity = DepositTransactionHistoryEntity.builder()
			.id(3L)
			.type(DepositTransactionType.REFUND)
			.build();
		DepositTransactionHistoryEntity chargeEntity = DepositTransactionHistoryEntity.builder()
			.id(4L)
			.type(DepositTransactionType.CHARGE)
			.build();
		DepositTransactionHistoryEntity chargeEntity2 = DepositTransactionHistoryEntity.builder()
			.id(4L)
			.amount(4000)
			.type(DepositTransactionType.CHARGE)
			.build();

		Assertions.assertTrue(chargeEntity.equals(chargeEntity2));
		Assertions.assertFalse(chargeEntity.equals(refundEntity));
	}
}
