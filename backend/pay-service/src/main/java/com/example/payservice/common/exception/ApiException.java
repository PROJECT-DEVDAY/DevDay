package com.example.payservice.common.exception;

public class ApiException extends RuntimeException {
    private final ExceptionEnum error;

    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }

    public ExceptionEnum getError() {
        return error;
    }
}
