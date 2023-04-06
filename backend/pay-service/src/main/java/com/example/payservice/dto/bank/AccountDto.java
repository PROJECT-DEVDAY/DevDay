package com.example.payservice.dto.bank;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AccountDto {
    @NotBlank
    @Size(max = 3, min = 3, message = "은행코드는 3자리 입니다.")
    private String bankCode;
    // 계좌번호
    @NotBlank
    @Size(max = 16, message = "계좌번호 최대길이는 16자리 입니다.")
    private String number;
    // 예금주
    @NotBlank
    private String depositor;
}
