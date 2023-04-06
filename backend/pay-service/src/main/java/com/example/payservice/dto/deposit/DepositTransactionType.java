package com.example.payservice.dto.deposit;

/**
 * 예치금 처리 타입
 */
public enum DepositTransactionType {
	/** 환불 */
	REFUND,
	/** 지불 */
	PAY,
	/** 충전 */
	CHARGE,
	/** 카드 부분취소 */
	CANCEL
}
