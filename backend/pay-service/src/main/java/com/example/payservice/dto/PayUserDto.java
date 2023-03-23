package com.example.payservice.dto;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class PayUserDto {

    private Long userId;
    @Positive
    private Integer prize;
    @Positive
    private Integer deposit;
}
