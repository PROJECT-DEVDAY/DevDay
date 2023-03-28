package com.example.payservice.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.payservice.dto.user.PayUserDto;

@DataJpaTest
@ActiveProfiles("test")
class DepositTransactionEntityTest {

	@Test
	@DisplayName("DepositTransactionEntity equals 테스트")
	void DespositTransactionEqualsTest() {
		PayUserEntity user = new PayUserEntity(1L, 0, 0);
		DepositTransactionEntity entity1ByUser = DepositTransactionEntity.builder()
			.id("1")
			.refundableAmount(3000)
			.amount(5000)
			.user(user)
			.paymentKey("paymentKey-1")
			.build();
		DepositTransactionEntity entity2ByUser = DepositTransactionEntity.builder()
			.id("2")
			.refundableAmount(3000)
			.amount(5000)
			.user(user)
			.paymentKey("paymentKey-1")
			.build();
		PayUserEntity user2 = new PayUserEntity(2L, 500, 0);
		DepositTransactionEntity entity1ByUser2 = DepositTransactionEntity.builder()
			.id("1")
			.refundableAmount(3000)
			.amount(5000)
			.user(user2)
			.paymentKey("paymentKey-1")
			.build();

		Assertions.assertNotEquals(entity1ByUser, entity2ByUser);
		Assertions.assertNotEquals(entity1ByUser, entity1ByUser2);
	}
}
