package com.example.payservice.exception;

import java.util.Map;

public class ChallengeServiceException extends Exception {

    private String code;
    private String message;
    public ChallengeServiceException(String msg) {
        super(msg);
        this.message = msg;
    }

    public ChallengeServiceException(Map<String, String> map) {
        super(map.getOrDefault("message", "챌린지 서비스에서 문제가 발생했습니다."));
        this.code = map.getOrDefault("code", "0000");
        this.message = map.getOrDefault("message", "챌린지 서비스에서 문제가 발생했습니다.");
    }

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
