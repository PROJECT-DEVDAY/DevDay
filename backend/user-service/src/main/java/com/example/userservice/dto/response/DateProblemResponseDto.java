package com.example.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DateProblemResponseDto {

    private Long userId;

    private String problemId;

    private String date;

    public DateProblemResponseDto(Long userId, String problemId, String date) {
        this.userId = userId;
        this.problemId = problemId;
        this.date = date;
    }
}
