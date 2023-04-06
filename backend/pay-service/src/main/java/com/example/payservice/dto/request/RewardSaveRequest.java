package com.example.payservice.dto.request;

import lombok.Data;

@Data
public class RewardSaveRequest {
    private long userId;
    private long challengeId;
    private int amount;
}
