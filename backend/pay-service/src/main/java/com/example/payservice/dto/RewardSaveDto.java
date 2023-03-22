package com.example.payservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RewardSaveDto {
    private long userId;
    private long challengeId;
    private int amount;
}
