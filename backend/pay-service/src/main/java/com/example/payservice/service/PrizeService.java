package com.example.payservice.service;

import com.example.payservice.dto.AccountDto;
import com.example.payservice.dto.RewardSaveDto;

public interface PrizeService {
    // 상금 환급하기
    boolean withdraw(long userId, int money, AccountDto account) throws Exception;

    // 상금 저장하기
    void save(RewardSaveDto rewardSaveDto);
}
