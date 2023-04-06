package com.example.challengeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DateProblemResponseDto {

    private String problemId;

    private Long userId;

    private String successDate;
}
