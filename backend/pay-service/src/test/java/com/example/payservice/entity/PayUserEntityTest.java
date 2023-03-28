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
	@DisplayName("PayUserEntity가 같은 지 비교하기")
	void equalsTest() {
		PayUserEntity user1 = new PayUserEntity(1L, 500, 0);
		PayUserEntity user2 = new PayUserEntity(1L, 4500, 0);
		PayUserEntity user3 = new PayUserEntity(2L, 4500, 0);

		Assertions.assertEquals(user1, user2);
		Assertions.assertNotEquals(user1, user3);
	}
}
