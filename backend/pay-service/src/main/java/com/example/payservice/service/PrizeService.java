package com.example.payservice.service;

import com.example.payservice.dto.AccountDto;
import com.example.payservice.dto.PrizeHistoryDto;
import com.example.payservice.dto.RewardSaveDto;
import com.example.payservice.dto.response.WithdrawResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrizeService {
    // 상금 환급하기
    WithdrawResponse withdraw(long userId, int money, AccountDto account) throws Exception;

    // 상금 저장하기
    void save(RewardSaveDto rewardSaveDto);

    Page<PrizeHistoryDto> searchHistories(Long userId, String type, Pageable pageable);
}
