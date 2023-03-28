package com.example.payservice.dto.deposit;

import com.example.payservice.entity.DepositTransactionEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepositWithdrawTransaction {
	private Integer withdrawMoney;
	private DepositTransactionEntity transaction;
}
