package com.example.userservice.service;

import com.example.userservice.dto.request.DateProblemRequestDto;
import com.example.userservice.dto.request.ProblemRequestDto;
import com.example.userservice.dto.response.BaekjoonListResponseDto;
import com.example.userservice.dto.response.DateProblemResponseDto;

import java.util.List;

public interface ChallengeService {

    BaekjoonListResponseDto getBaekjoonList(Long userId);

    void createProblem(Long userId, ProblemRequestDto requestDto);

    List<DateProblemResponseDto> getDateBaekjoonList(Long userId, DateProblemRequestDto requestDto);
}
