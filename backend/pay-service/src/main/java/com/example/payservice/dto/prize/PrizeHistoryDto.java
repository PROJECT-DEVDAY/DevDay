package com.example.payservice.dto.prize;

import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.entity.PrizeHistoryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Data
@Builder
public class PrizeHistoryDto {
    @JsonProperty("history_id")
    private String id;
    @JsonProperty("type")
    private PrizeHistoryType prizeHistoryType;
    private Long userId;
    private Long challengeId;

    private Integer amount;

    private AccountDto account;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static PrizeHistoryDto from(PrizeHistoryEntity entity) {
        return PrizeHistoryDto
                .builder()
                .account(new ModelMapper().map(entity.getAccountEntity(), AccountDto.class))
                .id(entity.getId())
                .challengeId(entity.getChallengeId())
                .amount(entity.getAmount())
                .prizeHistoryType(entity.getPrizeHistoryType())
                .userId(entity.getUser().getUserId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
