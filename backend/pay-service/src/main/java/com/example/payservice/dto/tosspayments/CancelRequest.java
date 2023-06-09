package com.example.payservice.dto.tosspayments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequest implements Serializable {
	private String cancelReason;
	private Integer cancelAmount;

	public CancelRequest(int cancelAmount) {
		this("devday 고객환불 요청", cancelAmount);
	}

	public CancelRequest(String cancelReason, int cancelAmount) {
		this.cancelReason = cancelReason;
		this.cancelAmount = cancelAmount;
	}
}
