package com.example.payservice.dto.request;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class WithdrawDepositRequest {
	@Positive
	private Integer money;
}
