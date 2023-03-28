package com.example.payservice.exception;

public class PaymentCancelException extends RuntimeException{
	public PaymentCancelException(String msg) {
		super(msg);
	}
}
