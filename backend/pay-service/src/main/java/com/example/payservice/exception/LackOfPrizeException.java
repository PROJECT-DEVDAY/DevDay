package com.example.payservice.exception;

public class LackOfPrizeException extends RuntimeException {

    public LackOfPrizeException(String message) {
        super(message);
    }
}
