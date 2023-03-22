package com.example.payservice.vo.internal;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class PayUser {

    private Long userId;
    @Positive
    private Integer prize;
    @Positive
    private Integer deposit;
}
