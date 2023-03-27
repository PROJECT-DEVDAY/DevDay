package com.example.payservice.exception;

public class UnRefundableException extends RuntimeException {
    public UnRefundableException(String msg) {
        super(msg);
    }
}
