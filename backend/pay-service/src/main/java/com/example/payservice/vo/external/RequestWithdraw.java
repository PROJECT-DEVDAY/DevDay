package com.example.payservice.vo.external;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class RequestWithdraw {
    private Account account;
    @Positive
    private Integer money;
}
