package com.example.userservice.service;

import com.example.userservice.dto.request.challenge.CommitRequestDto;
import com.example.userservice.dto.request.challenge.ProblemRequestDto;
import com.example.userservice.dto.response.challenge.BaekjoonListResponseDto;
import com.example.userservice.dto.response.challenge.CommitResponseDto;
import com.example.userservice.dto.response.challenge.DateProblemResponseDto;

import java.util.List;

public interface ChallengeService {

    BaekjoonListResponseDto getBaekjoonList(Long userId);

    void createProblem(Long userId, ProblemRequestDto requestDto);

    List<DateProblemResponseDto> getDateBaekjoonList(Long userId, String startDate, String endDate);

    void updateCommitCount(Long userId, CommitRequestDto requestDto);

    CommitResponseDto getCommitRecord(Long userId, String commitDate);
    List<CommitResponseDto> getDateCommitList(Long userId, String startDate, String endDate);
}
