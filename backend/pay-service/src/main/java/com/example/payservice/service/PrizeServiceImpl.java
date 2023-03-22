package com.example.payservice.service;

import com.example.payservice.client.UserServiceClient;
import com.example.payservice.dto.AccountDto;
import com.example.payservice.dto.RewardSaveDto;
import com.example.payservice.entity.UserEntity;
import com.example.payservice.entity.bank.AccountEntity;
import com.example.payservice.entity.prize.PrizeHistoryEntity;
import com.example.payservice.repository.PrizeHistoryRepository;
import com.example.payservice.repository.UserRepository;
import com.example.payservice.vo.internal.ResponseUser;
import com.example.payservice.vo.prize.PrizeHistoryType;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class PrizeServiceImpl implements PrizeService {

    private final UserServiceClient userServiceClient;
    private final UserRepository userRepository;
    private final PrizeHistoryRepository prizeHistoryRepository;

    @Override
    @Transactional
    public boolean withdraw(long userId, int money, AccountDto account) throws Exception {
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
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity.getPrize() < money) {
            throw new Exception("출금할 상금 금액이 저장된 금액보다 큽니다.");
        }
        /*
            TODO: [우선순위 낮음] 요청한 계좌가 유효한지 확인한다.
            계좌 유효 검사 시에 주민등록 앞자리가 필요함
        */

        // TODO: 출금을 반영합니다.


        // 출금이력을 기록합니다.
        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.builder()
                .userId(userId)
                .prizeHistoryType(PrizeHistoryType.OUT)
                .amount(money)
                .accountEntity(new ModelMapper().map(account, AccountEntity.class))
                .build();

        // transaction 반영
        userEntity.setPrize(userEntity.getPrize() - money);
        prizeHistoryRepository.save(prizeHistory);

        return true;
    }

    @Override
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
        UserEntity userEntity = userRepository.findByUserId(rewardSaveDto.getUserId());

        // TODO: 챌린지 ID가 존재하는 지

        PrizeHistoryEntity prizeHistory = PrizeHistoryEntity.builder()
                .userId(userEntity.getUserId())
                .challengeId(rewardSaveDto.getChallengeId())
                .prizeHistoryType(PrizeHistoryType.IN)
                .amount(rewardSaveDto.getAmount())
                .build();

        // transaction 반영
        userEntity.setPrize(userEntity.getPrize() + rewardSaveDto.getAmount());
        prizeHistoryRepository.save(prizeHistory);
    }
}
