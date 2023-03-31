package com.example.userservice.service;

import com.example.userservice.dto.request.challenge.ProblemRequestDto;
import com.example.userservice.dto.response.challenge.BaekjoonListResponseDto;
import com.example.userservice.dto.response.challenge.DateProblemResponseDto;

import java.util.List;

public interface ChallengeService {

    BaekjoonListResponseDto getBaekjoonList(Long userId);

    void createProblem(Long userId, ProblemRequestDto requestDto);

    List<DateProblemResponseDto> getDateBaekjoonList(Long userId, String startDate, String endDate);
}
