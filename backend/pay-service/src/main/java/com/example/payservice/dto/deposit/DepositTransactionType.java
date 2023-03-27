package com.example.payservice.dto.deposit;

public enum DepositTransactionType {
	// 환불
	REFUND,
	// 지불
	PAY,
	// 충전
	CHARGE,
	// 카드 부분취소
	CANCEL
}
