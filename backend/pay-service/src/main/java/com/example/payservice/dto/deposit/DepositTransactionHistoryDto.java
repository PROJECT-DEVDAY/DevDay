package com.example.payservice.dto.deposit;


import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepositTransactionHistoryDto {
    @JsonProperty("transaction_history_id")
    private Long id;

    @JsonProperty("type")
    private DepositTransactionType type;

    private Long userId;

    private Integer amount;

    private SimpleChallengeInfo challenge;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static DepositTransactionHistoryDto from(DepositTransactionHistoryEntity entity) {
        return DepositTransactionHistoryDto
                .builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .createdAt(entity.getCreatedAt())
                .userId(entity.getUser().getUserId())
                .type(entity.getType())
                .build();
    }
}
