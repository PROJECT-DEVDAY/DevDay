package com.example.payservice.exception;

public class AlreadyRefundableException extends RuntimeException {
    public AlreadyRefundableException(String msg) {
        super(msg);
    }
}
