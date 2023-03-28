package com.example.payservice.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class PayUserEntityTest {

	@Test
	@DisplayName("PayUserEntity equals를 비교합니다. - userId만 같을 경우")
	void 유저_비교_예치금_차이() {
		PayUserEntity user1 = new PayUserEntity(1L, 500, 0);
		PayUserEntity user2 = new PayUserEntity(1L, 4500, 0);

		Assertions.assertEquals(user1, user2);
	}

	@Test
	@DisplayName("PayUserEntity equals를 비교합니다. - userId만 다를 경우")
	void 유저_비교_아이디_차이() {
		PayUserEntity user1 = new PayUserEntity(1L, 500, 0);
		PayUserEntity user2 = new PayUserEntity(2L, 500, 0);

		Assertions.assertNotEquals(user1, user2);
	}
}
