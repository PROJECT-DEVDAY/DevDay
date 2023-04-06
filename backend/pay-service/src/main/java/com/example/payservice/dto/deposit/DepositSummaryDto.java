package com.example.payservice.dto.deposit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositSummaryDto {
	/** 총 충전 비용 */
	private int charge;
	/** 총 카드 환급 비용 */
	private int cancel;

	/** 현재 챌린지 참가 비용 */
	private int challenging;

	/** 벌금 비용 */
	private int penalty;
}
