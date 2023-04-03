package com.example.challengeservice.dto.response;


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

}
