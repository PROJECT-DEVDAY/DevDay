package com.example.userservice.repository;

import com.example.userservice.dto.response.DateProblemResponseDto;

import java.util.List;

public interface SolvedacRepositoryCustom {
    List<DateProblemResponseDto> getDateProblem(Long userId, String startDate, String endDate);
}
