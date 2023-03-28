package com.example.payservice.exception;

public class LackOfDepositException extends RuntimeException {

	public LackOfDepositException(String message) {
		super(message);
	}
}
