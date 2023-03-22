package com.example.payservice.dto;

import lombok.Data;

@Data
public class PayUserDto {
    private Long userId;
    private Integer prize;
    private Integer deposit;
}
