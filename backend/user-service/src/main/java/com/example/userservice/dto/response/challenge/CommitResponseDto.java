package com.example.userservice.dto.response.challenge;

import com.example.userservice.dto.request.challenge.CommitRequestDto;
import com.example.userservice.entity.CommitRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitResponseDto {

    private String commitDate;

    private Long userId;

    private int commitCount;

    public static CommitResponseDto from (CommitRecord commitRecord) {
        return CommitResponseDto.builder()
                .commitDate(commitRecord.getCommitDate())
                .userId(commitRecord.getUser().getId())
                .commitCount(commitRecord.getCommitCount())
                .build();
    }
}
