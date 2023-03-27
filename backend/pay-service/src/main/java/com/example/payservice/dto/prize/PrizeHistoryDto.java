package com.example.payservice.dto.prize;

import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.challenge.SimpleChallengeInfo;
import com.example.payservice.entity.PrizeHistoryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrizeHistoryDto {
    @JsonProperty("history_id")
    private String id;
    @JsonProperty("type")
    private PrizeHistoryType prizeHistoryType;
    private Long userId;
    private SimpleChallengeInfo challenge;

    private Integer amount;

    private AccountDto account;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * PrizeHistoryEntity를 PrizeHistoryDto로 변환하는 작업을 합니다.
     * 단, 사용시 주의할 점은 challenge에 대한 정보를 넣고 있지 않습니다.
     * 따라서 challenge는 직접 넣어주세요!
     *
     * @author djunnni
     * @param entity
     * @return
     */
    public static PrizeHistoryDto from(PrizeHistoryEntity entity) {
        PrizeHistoryDto dto = PrizeHistoryDto
                .builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .prizeHistoryType(entity.getPrizeHistoryType())
                .userId(entity.getUser().getUserId())
                .createdAt(entity.getCreatedAt())
                .build();

        if(entity.getPrizeHistoryType() == PrizeHistoryType.OUT) {
            dto.setAccount(new ModelMapper().map(entity.getAccountEntity(), AccountDto.class));
        }

        return dto;
    }
}
