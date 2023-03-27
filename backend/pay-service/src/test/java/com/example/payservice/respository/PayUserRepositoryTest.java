package com.example.payservice.respository;

import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.repository.PayUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class PayUserRepositoryTest {

    @Mock
    PayUserRepository payUserRepository;

    @Test
    @DisplayName("pay-service 유저 삭제")
    public void 유저_삭제() {
        // give
        PayUserEntity entity = new PayUserEntity();
        entity.setUserId(1L);
        entity.updateDeposit(2000);
        entity.updatePrize(3000);
        payUserRepository.save(entity);

        // when
        payUserRepository.deleteByUserId(1L);

        // then
        PayUserEntity result = payUserRepository.findByUserId(1L);
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("pay-service 유저 2번 삭제")
    public void 유저_2번_삭제() {
        // give
        PayUserEntity entity = new PayUserEntity();
        entity.setUserId(1L);
        entity.updateDeposit(2000);
        entity.updatePrize(3000);
        payUserRepository.save(entity);

        // when
        Assertions.assertDoesNotThrow(() -> {
            payUserRepository.deleteByUserId(1L);
            payUserRepository.deleteByUserId(1L);
        });
    }
}
