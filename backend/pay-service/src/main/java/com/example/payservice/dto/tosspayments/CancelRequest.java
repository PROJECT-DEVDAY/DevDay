package com.example.payservice.dto.tosspayments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequest {
	private String cancelReason;
	private Integer cancelAmount;

	public CancelRequest(int cancelAmount) {
		this.cancelReason = "devday 고객환불 요청";
		this.cancelAmount = cancelAmount;
	}
}
