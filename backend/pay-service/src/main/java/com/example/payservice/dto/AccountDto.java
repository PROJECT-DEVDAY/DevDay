package com.example.payservice.dto;

import lombok.Data;

@Data
public class AccountDto {
    // 은행코드
    private String bankCode;
    // 계좌번호
    private String number;
    // 예금주
    private String depositor;
}
