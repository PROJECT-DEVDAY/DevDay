package com.example.payservice.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * 토스페이, 농협오픈뱅킹과 관련된 서비스를 처리합니다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final Environment env;

    private final UserService userService;
    // private final DepositTransactionRepository depositTransactionRepository;
    private final DepositTransactionHistoryRepository depositTransactionHistoryRepository;

    /**
     * 토스로부터 결제가 완료되고 결제정보와 히스토리를 남깁니다.
     * 히스토리는 충전과 동시에 사용한 여부를 저장하고 있어 2가지가 처리됩니다.
     *
     * @param payment
     * @param userId
     * @param challengeId
     * @return
     */
    @Transactional
    public ChallengeJoinResponse saveTransaction(Payment payment, Long userId, Long challengeId) {
        PayUserEntity userEntity = userService.getPayUserEntityForUpdate(userId);

        DepositTransactionEntity dtEntity = createTransaction(payment, userEntity);
        saveChargeTransactionHistory(payment, userEntity, dtEntity);
        savePayTransactionHistory(payment, userEntity, challengeId);

        return ChallengeJoinResponse.builder()
                .userId(userId)
                .challengeId(challengeId)
                .approve(true)
                .build();
    }

    /**
     * 결제 내역을 Transaction으로 생성합니다.
     * @param payment
     * @param user
     * @return
     */
    private DepositTransactionEntity createTransaction(Payment payment, PayUserEntity user) {
        return DepositTransactionEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .user(user)
                .paymentKey(payment.getPaymentKey())
                .amount(payment.getTotalAmount())
                .refundableAmount(payment.getTotalAmount())
                .build();
    }

    /**
     * 충전한 트랜잭션 히스토리를 저장합니다.
     * @param payment
     * @param user
     * @param dtEntity
     */
    private void saveChargeTransactionHistory(Payment payment, PayUserEntity user, DepositTransactionEntity dtEntity) {
        DepositTransactionHistoryEntity chargeDthEntity = DepositTransactionHistoryEntity.builder()
                .depositTransaction(dtEntity)
                .type(DepositTransactionType.CHARGE)
                .amount(payment.getTotalAmount())
                .user(user)
                .build();
        depositTransactionHistoryRepository.save(chargeDthEntity);
    }

    /**
     * 지불한 트랜잭션 히스토리를 기록합니다.w
     * @param payment
     * @param user
     * @param challengeId
     */
    private void savePayTransactionHistory(Payment payment, PayUserEntity user, Long challengeId) {
        DepositTransactionHistoryEntity payDthEntity = DepositTransactionHistoryEntity.builder()
                .challengeId(challengeId)
                .type(DepositTransactionType.PAY)
                .amount(payment.getTotalAmount())
                .user(user)
                .build();
        depositTransactionHistoryRepository.save(payDthEntity);
    }
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
