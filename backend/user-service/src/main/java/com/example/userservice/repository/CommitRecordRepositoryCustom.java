package com.example.userservice.repository;

import com.example.userservice.dto.response.challenge.CommitResponseDto;

import java.util.List;

public interface CommitRecordRepositoryCustom {
    List<CommitResponseDto> getDateCommit(Long userId, String startDate, String endDate);
}
