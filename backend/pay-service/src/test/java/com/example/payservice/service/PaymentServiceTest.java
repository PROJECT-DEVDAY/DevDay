package com.example.payservice.service;

import com.example.payservice.dto.response.ChallengeJoinResponse;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.repository.DepositTransactionHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class PaymentServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private DepositTransactionHistoryRepository depositTransactionHistoryRepository;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제/결제 히스토리 이력 저장하기")
    void 결제_결제_히스토리_저장하기() {
        // give
        PayUserEntity entity = new PayUserEntity(1L, 2000, 3000);

        Payment payment = new Payment();
        payment.setPaymentKey("paymentKey-1");
        payment.setTotalAmount(5000);
        // when
        when(userService.getPayUserEntityForUpdate(1L)).thenReturn(entity);

        // then
        Assertions.assertEquals(
                paymentService.saveTransaction(payment, 1L, 2L),
                ChallengeJoinResponse.builder()
                .userId(1L)
                .challengeId(2L)
                .approve(true)
                .build());
    }

}
