package com.example.payservice.service.prize;

import com.example.payservice.client.ChallengeServiceClient;
import com.example.payservice.client.UserServiceClient;
import com.example.payservice.dto.AccountDto;
import com.example.payservice.dto.PrizeHistoryDto;
import com.example.payservice.dto.RewardSaveDto;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.entity.AccountEntity;
import com.example.payservice.entity.PrizeHistoryEntity;
import com.example.payservice.dto.Bank;
import com.example.payservice.repository.PayUserRepository;
import com.example.payservice.repository.PrizeHistoryRepository;

import com.example.payservice.dto.PrizeHistoryType;
import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.dto.nhbank.Header;
import com.example.payservice.dto.nhbank.RequestTransfer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PrizeService {

    private final Environment env;

    private final ChallengeServiceClient challengeServiceClient;
    private final UserServiceClient userServiceClient;
    private final PayUserRepository payUserRepository;
    private final PrizeHistoryRepository prizeHistoryRepository;

    @Transactional
    public WithdrawResponse withdraw(long userId, int money, AccountDto account) throws Exception {
        log.info("accountDto 조회하기 -> {}", account);
        // 유저의 존재여부를 확인한다.
//        ResponseUser responseUser = null;
//        try {
//            responseUser = userServiceClient.getUserInfo(userId);
//            if(responseUser == null) {
//                throw new Exception("등록되지 않은 유저입니다.");
//            }
//        } catch(FeignException ex) {
//            throw new Exception("유저 서비스에서 정보를 가져오는데 실패했습니다.");
//        }
        // 유저의 출금가능금액과 요청 금액을 비교해본다.
        PayUserEntity payUserEntity = payUserRepository.findByUserId(userId);
        if(payUserEntity.getPrize() < money) {
            throw new Exception("출금할 상금 금액이 저장된 금액보다 큽니다.");
        }
        /*
            TODO: [우선순위 낮음] 요청한 계좌가 유효한지 확인한다.
            계좌 유효 검사 시에 주민등록 앞자리가 필요함
        */

        // 출금을 반영합니다.
        boolean result = transferMoney(account, money);
        // 출금이력을 기록합니다.
        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .prizeHistoryType(PrizeHistoryType.OUT)
                .amount(money)
                .accountEntity(new ModelMapper().map(account, AccountEntity.class))
                .build();

        // transaction 반영
        prizeHistory.setUser(payUserEntity);
        prizeHistoryRepository.save(prizeHistory);

        WithdrawResponse response = new WithdrawResponse(result, payUserEntity.getPrize());
        return response;
    }

    @Transactional
    public void save(RewardSaveDto rewardSaveDto) {
        // TODO: 유저 ID가 존재하는 지
        //        ResponseUser responseUser = null;
//        try {
//            responseUser = userServiceClient.getUserInfo(userId);
//            if(responseUser == null) {
//                throw new Exception("등록되지 않은 유저입니다.");
//            }
//        } catch(FeignException ex) {
//            throw new Exception("유저 서비스에서 정보를 가져오는데 실패했습니다.");
//        }
        // 유저의 출금가능금액과 요청 금액을 비교해본다.
        PayUserEntity payUserEntity = payUserRepository.findByUserId(rewardSaveDto.getUserId());

        // TODO: 챌린지 ID가 존재하는 지

        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .challengeId(rewardSaveDto.getChallengeId())
                .prizeHistoryType(PrizeHistoryType.IN)
                .amount(rewardSaveDto.getAmount())
                .build();

        // transaction 반영
        prizeHistory.setUser(payUserEntity);
        prizeHistoryRepository.save(prizeHistory);
    }

    public Page<PrizeHistoryDto> searchHistories(Long userId, String type, Pageable pageable) {
        PayUserEntity user = payUserRepository.findByUserId(userId);

        // TODO: 챌린지 정보 반영하기
        Page<PrizeHistoryDto> pages =  prizeHistoryRepository
                .findAllByUserAndPrizeHistoryType(user, String.valueOf(type).isEmpty() ? null : type, pageable)
                .map(PrizeHistoryDto::from);

        return pages;
    }

    private boolean transferMoney(AccountDto accountDto, int money) {
        WebClient client = WebClient.builder()
                .baseUrl(env.getProperty("openapi.nonghyup.baseUrl"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String bankCode = accountDto.getBankCode();
        if(
                Bank.NONGHYEOP.getCode().equals(bankCode) ||
                Bank.LOCALNONGHYEOP.getCode().equals(bankCode)
        ) {
            return transferMoneyNH(client, accountDto, money);
        }

        return transferMoneyNotNH(client, accountDto, money);

    }
    private boolean transferMoneyNH(WebClient client, AccountDto account, int money) {
        RequestTransfer transfer = createNHApiRequestTransfer(
                createNHApiHeader("ReceivedTransferOtherBank"),
                money,
                account
        );

        Header header = client.post().uri("/ReceivedTransferAccountNumber.nh")
                .bodyValue(transfer).retrieve().bodyToMono(Header.class).block();

        return true;
    }
    private boolean transferMoneyNotNH(WebClient client, AccountDto account, int money) {
        RequestTransfer transfer = createNHApiRequestTransfer(
                createNHApiHeader("ReceivedTransferOtherBank"),
                money,
                account
        );
        log.info("transferMoneyNotNH -> {}", transfer);
        Header header = client.post().uri("/ReceivedTransferOtherBank.nh").bodyValue(transfer)
                .retrieve().onStatus(HttpStatus::isError, response ->
                    response.bodyToMono(String.class) // error body as String or other class
                            .flatMap(error -> Mono.error(new RuntimeException(error)))
                ).bodyToMono(Header.class).block();
        return true;
    }
    private Header createNHApiHeader(String apiNm) {
        Date now = new Date();
        String dateTime = new SimpleDateFormat("yyyyMMddHHmmssSSSS")
                .format(now);
        return Header.builder()
                .apiNm(apiNm)
                .tsymd(dateTime.substring(0, 8))
                .trtm(dateTime.substring(8, 14))
                .iscd(env.getProperty("openapi.nonghyup.iscd"))
                .fintechApsno(env.getProperty("openapi.nonghyup.fintechApsno"))
                .apiSvcCd(env.getProperty("openapi.nonghyup.apiSvcCd"))
                .isTuno(String.valueOf(now.getTime()))
                .accessToken(env.getProperty("openapi.nonghyup.accessToken"))
                .build();
    }

    private RequestTransfer createNHApiRequestTransfer(
            Header header,
            int money,
            AccountDto accountDto
    ) {
        return RequestTransfer.builder()
                .header(header)
                .bncd(accountDto.getBankCode())
                .acno(accountDto.getNumber())
                .tram(String.valueOf(money))
                .dractOtlt(env.getProperty("openapi.nonghyup.otlt") + " " + money + "원")
                .mractOtlt(env.getProperty("openapi.nonghyup.otlt") + " " + money + "원")
                .build();
    }
}
