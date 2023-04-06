package com.example.payservice.dto.tosspayments;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentInfo implements Serializable {
	private String paymentKey;
	private String orderId;
	private Integer amount;
}
