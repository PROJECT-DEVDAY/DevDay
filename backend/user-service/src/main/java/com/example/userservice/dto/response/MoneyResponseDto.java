package com.example.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyResponseDto {

    private Long userId;

    @Positive
    private Integer prize;

    @Positive
    private Integer deposit;
}
