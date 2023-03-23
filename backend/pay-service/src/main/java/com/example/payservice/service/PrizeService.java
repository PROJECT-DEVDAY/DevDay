package com.example.payservice.service;

import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.prize.PrizeHistoryDto;
import com.example.payservice.dto.request.RewardSaveRequest;
import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.entity.PrizeHistoryEntity;
import com.example.payservice.exception.LackOfPrizeException;
import com.example.payservice.repository.PrizeHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class PrizeService {
    private final PrizeHistoryRepository prizeHistoryRepository;
    private final PaymentService paymentService;
    private final UserService userService;

    /**
     * 상금을 환급하고 히스토리에 이력을 남깁니다.
     * @param userId
     * @param money
     * @param account
     * @return
     * @throws Exception
     */
    @Transactional
    public WithdrawResponse withdraw(long userId, int money, AccountDto account) {

        // 유저의 출금가능금액과 요청 금액을 비교해본다.
        PayUserEntity payUserEntity = userService.getPayUserEntity(userId);
        checkDrawMoney(payUserEntity, money);
        /*
            TODO: [우선순위 낮음] 요청한 계좌가 유효한지 확인한다.
            계좌 유효 검사 시에 주민등록 앞자리가 필요함
        */

        // 출금을 반영합니다.
        boolean result = paymentService.transferMoney(account, money);
        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.createOutTypePrizeHistory(account, money);

        // transaction 반영
        prizeHistory.setUser(payUserEntity);
        prizeHistoryRepository.save(prizeHistory);

        return new WithdrawResponse(result, payUserEntity.getPrize());
    }

    /**
     * 상금 획득 이력을 남깁니다.
     * @param request
     */
    @Transactional
    public void save(RewardSaveRequest request) {
        PayUserEntity payUserEntity = userService.getPayUserEntity(request.getUserId());
        // TODO: 챌린지 ID가 존재하는 지
        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.createInTypePrizeHistory(request);

        // transaction 반영
        prizeHistory.setUser(payUserEntity);
        prizeHistoryRepository.save(prizeHistory);
    }

    /**
     * 유저의 환급/획득(in/out) 이력을 Page로 얻습니다.
     * @param userId
     * @param type
     * @param pageable
     * @return
     */
    public Page<PrizeHistoryDto> searchHistories(long userId, String type, Pageable pageable) {
        PayUserEntity payUserEntity = userService.getPayUserEntity(userId);
        String historyType = String.valueOf(type).isEmpty() ? null : type;
        // TODO: 챌린지 정보 반영하기

        return prizeHistoryRepository
                .findAllByUserAndPrizeHistoryType(payUserEntity, historyType, pageable)
                .map(PrizeHistoryDto::from);
    }

    /**
     * 유저가 money를 출금할 수 있는지 확인합니다.
     * @param payUserEntity
     * @param money
     * @throws Exception
     */
    private void checkDrawMoney(PayUserEntity payUserEntity, int money) {
        if(payUserEntity.getPrize() < money) {
            throw new LackOfPrizeException("출금할 상금 금액이 저장된 금액보다 큽니다.");
        }
    }
}
