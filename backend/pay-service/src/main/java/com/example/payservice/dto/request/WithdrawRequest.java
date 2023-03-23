package com.example.payservice.dto.request;

import com.example.payservice.dto.AccountDto;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class WithdrawRequest {
    private AccountDto account;
    @Positive
    private Integer money;
}
