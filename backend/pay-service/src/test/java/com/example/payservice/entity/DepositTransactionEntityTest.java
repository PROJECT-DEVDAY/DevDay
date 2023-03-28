package com.example.payservice.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class DepositTransactionEntityTest {

	@Test
	@DisplayName("equals 테스트 - id가 다를 경우")
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

		Assertions.assertEquals(false, entity1ByUser.equals(entity2ByUser));
	}
	@Test
	@DisplayName("equals 테스트 - 같은 유저 ID이되 예치금이 다를 경우")
	void DespositTransactionEqualsTest2() {
		PayUserEntity user = new PayUserEntity(1L, 0, 0);
		DepositTransactionEntity entity1ByUser = DepositTransactionEntity.builder()
				.id("1")
				.refundableAmount(3000)
				.amount(5000)
				.user(user)
				.paymentKey("paymentKey-1")
				.build();
		PayUserEntity user2 = new PayUserEntity(1L, 500, 0);
		DepositTransactionEntity entity1ByUser2 = DepositTransactionEntity.builder()
				.id("1")
				.refundableAmount(3000)
				.amount(5000)
				.user(user2)
				.paymentKey("paymentKey-1")
				.build();
		Assertions.assertEquals(true, entity1ByUser.equals(entity1ByUser2));
	}

	@Test
	@DisplayName("equals 테스트 - 유저가 다를 경우")
	void DespositTransactionEqualsTest3() {
		PayUserEntity user = new PayUserEntity(1L, 0, 0);
		DepositTransactionEntity entity1ByUser = DepositTransactionEntity.builder()
				.id("1")
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
		Assertions.assertEquals(false, entity1ByUser.equals(entity1ByUser2));
	}

	@Test
	@DisplayName("equals 테스트 - 페이먼트 키가 다를 경우")
	void DespositTransactionEqualsTest4() {
		PayUserEntity user = new PayUserEntity(1L, 0, 0);
		DepositTransactionEntity entity1ByUser = DepositTransactionEntity.builder()
				.id("1")
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
				.paymentKey("paymentKey-2")
				.build();
		Assertions.assertEquals(false, entity1ByUser.equals(entity1ByUser2));
	}
}