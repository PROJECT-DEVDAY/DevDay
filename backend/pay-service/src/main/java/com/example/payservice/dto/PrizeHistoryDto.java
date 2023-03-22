package com.example.payservice.dto;

import com.example.payservice.vo.prize.PrizeHistoryType;
import lombok.Data;

@Data
public class PrizeHistoryDto {
    private Long id;
    private PrizeHistoryType prizeHistoryType;
    private Long userId;
    private Long challengeId;

    private Integer amount;

    private String withdrawId;
    private AccountDto account;
}
