package com.example.payservice.service;

import java.util.UUID;

import javax.transaction.Transactional;

import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.bank.Bank;
import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.dto.nhbank.Header;
import com.example.payservice.dto.nhbank.ReceivedTransferType;
import com.example.payservice.dto.nhbank.RequestTransfer;
import com.example.payservice.dto.response.ChallengeJoinResponse;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.dto.tosspayments.SuccessRequest;
import com.example.payservice.entity.DepositTransactionEntity;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.exception.PaymentsConfirmException;
import com.example.payservice.exception.PrizeWithdrawException;
import com.example.payservice.repository.DepositTransactionHistoryRepository;
import com.example.payservice.repository.DepositTransactionRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 토스페이, 농협오픈뱅킹과 관련된 서비스를 처리합니다.
 */

@Slf4j
@Service
@AllArgsConstructor
public class PaymentService {

    private final Environment env;

    private final UserService userService;
    private final DepositTransactionRepository depositTransactionRepository;
    private final DepositTransactionHistoryRepository depositTransactionHistoryRepository;

    @Transactional
    public ChallengeJoinResponse saveTransaction(Payment payment, Long userId, Long challengeId) {
        PayUserEntity userEntity = userService.getPayUserEntity(userId);
        DepositTransactionEntity dtEntity = DepositTransactionEntity.builder()
            .id(String.valueOf(UUID.randomUUID()))
            .user(userEntity)
            .paymentKey(payment.getPaymentKey())
            .amount(payment.getTotalAmount())
            .refundableAmount(payment.getTotalAmount())
            .build();

        DepositTransactionHistoryEntity dthEntity = DepositTransactionHistoryEntity.builder()
            .depositTransaction(dtEntity)
            .type(DepositTransactionType.PAY)
            .amount(payment.getTotalAmount())
            .user(userEntity)
            .build();

        userEntity.setDeposit(userEntity.getDeposit() + payment.getTotalAmount());
        depositTransactionHistoryRepository.save(dthEntity);


        return null;
    };
    /**
     * 사용자로부터 결제정보를 받아 토스에 확인 메시지를 전달합니다.
     * @param request
     * @return
     */
    public Payment confirm(SuccessRequest request) {
        WebClient client = WebClient.builder()
                .baseUrl(env.getProperty("payment.toss.baseUrl"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, env.getProperty("payment.toss.secret"))
                .build();

        return client.post().uri(env.getProperty("payment.toss.path.confirm"))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::isError, response ->
                        response.bodyToMono(String.class) // error body as String or other class
                                .flatMap(error -> Mono.error(new PaymentsConfirmException(error)))
                ).bodyToMono(Payment.class)
                .block();
    }


    /**
     * 결제가 취소/환급에 대해 메시지를 토스에게 전달합니다.
     * @return
     */
    public Payment cancel() {
        return null;
    }

    /**
     * 농협오픈플랫폼을 활용해 돈을 인출합니다.
     * @param accountDto
     * @param money
     * @return
     */
    public boolean transferMoney(AccountDto accountDto, int money) {
        WebClient client = WebClient.builder()
                .baseUrl(env.getProperty("openapi.nonghyup.baseUrl"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String bankCode = accountDto.getBankCode();
        ReceivedTransferType receivedTransferType = ReceivedTransferType.OTHER;
        if(
                Bank.NONGHYEOP.getCode().equals(bankCode) ||
                Bank.LOCALNONGHYEOP.getCode().equals(bankCode)
        ) {
            receivedTransferType = ReceivedTransferType.NH;
        }

        return transferMoney(receivedTransferType, client, accountDto, money);
    }

    /**
     * 농협, 그 외(신한, 우리 등) 계좌인지에 따라 송금합니다.
     * @param transferType
     * @param client
     * @param account
     * @param money
     * @return
     */
    private boolean transferMoney(
            ReceivedTransferType transferType,
            WebClient client,
            AccountDto account,
            int money
    ) {
        RequestTransfer transfer = RequestTransfer.createNHApiRequestTransfer(
                env,
                Header.createNHApiHeader(env, transferType.getApiNm()),
                account,
                money
        );
        client.post().uri(transferType.getUri()).bodyValue(transfer)
                .retrieve().onStatus(HttpStatus::isError, response ->
                        response.bodyToMono(String.class) // error body as String or other class
                                .flatMap(error -> Mono.error(new PrizeWithdrawException(error)))
                ).bodyToMono(Header.class).block();
        return true;
    }
}
