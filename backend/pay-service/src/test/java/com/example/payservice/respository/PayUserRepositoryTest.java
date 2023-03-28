package com.example.payservice.respository;

import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.repository.PayUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class PayUserRepositoryTest {
    static final int THREAD_COUNT = 50;
    ExecutorService executorService;
    CountDownLatch countDownLatch;
    @Autowired
    PayUserRepository payUserRepository;
    @BeforeEach
    public void beforeEach() {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        countDownLatch = new CountDownLatch(THREAD_COUNT);
    }
    @Test
    @Transactional
    @DisplayName("pay-service 유저 삭제")
    void 유저_삭제() {
        // give
        PayUserEntity entity = new PayUserEntity(1L, 2000, 3000);
        payUserRepository.save(entity);

        // when
        payUserRepository.deleteByUserId(1L);

        // then
        PayUserEntity result = payUserRepository.findByUserId(1L);
        Assertions.assertNull(result);
    }

    @Test
    @Transactional
    @DisplayName("pay-service 유저 2번 삭제")
    void 유저_2번_삭제() {
        // give
        PayUserEntity entity = new PayUserEntity(1L, 2000, 3000);
        payUserRepository.save(entity);

        // when
        Assertions.assertDoesNotThrow(() -> {
            payUserRepository.deleteByUserId(1L);
            payUserRepository.deleteByUserId(1L);
        });
    }
//    @Test
//    @Rollback(false)
//    @Transactional
//    @DisplayName("수정할 계정을 조회 - 멀티쓰레드")
//    void 수정할_계정_조회() throws InterruptedException {
//        // give
//        PayUserEntity user1 = new PayUserEntity(1L, THREAD_COUNT, 0);
//        payUserRepository.save(user1);
//
//        // when
//        IntStream.range(0, THREAD_COUNT).forEach(e ->
//                executorService.submit(() -> {
//                    try {
//                        PayUserEntity user = payUserRepository.findByUserIdForUpdate(1L);
//                        user.setDeposit(user.getDeposit() - 1);
//                        payUserRepository.save(user);
//                    } finally {
//                        countDownLatch.countDown();
//                    }
//                }));
//        countDownLatch.await();
//        // then
//        PayUserEntity user = payUserRepository.findByUserId(1L);
//        Assertions.assertEquals(0, user.getDeposit());
//    }
}
