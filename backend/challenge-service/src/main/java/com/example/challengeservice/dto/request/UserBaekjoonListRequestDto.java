package com.example.challengeservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBaekjoonListRequestDto {
    private Long userId;

    private Long problemId;

    private String successDate;
}