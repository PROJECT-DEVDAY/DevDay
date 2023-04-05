package com.example.challengeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProgressResponseDto {
    private String progressRate;
    private Long curPrice;
    private Long successCount;
    private Long failCount;
}
