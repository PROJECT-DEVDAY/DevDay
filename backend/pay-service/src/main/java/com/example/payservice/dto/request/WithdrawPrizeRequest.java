package com.example.payservice.dto.request;

import com.example.payservice.dto.bank.AccountDto;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class WithdrawPrizeRequest {
    private AccountDto account;
    @Positive
    private Integer money;
}
