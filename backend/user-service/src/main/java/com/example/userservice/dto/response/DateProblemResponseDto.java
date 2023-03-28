package com.example.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DateProblemResponseDto {

    private String problemId;

    private Long userId;

    private String successDate;

    public DateProblemResponseDto(String problemId, Long userId, String successDate) {
        this.problemId = problemId;
        this.userId = userId;
        this.successDate = successDate;
    }
}
