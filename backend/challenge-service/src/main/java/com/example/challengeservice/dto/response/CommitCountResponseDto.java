package com.example.challengeservice.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommitCountResponseDto {
    String githubId;
    int commitCount;
    public static CommitCountResponseDto from(String githubId,int commitCount){
        return CommitCountResponseDto.builder()
                .githubId(githubId)
                .commitCount(commitCount)
                .build();
    }
}
