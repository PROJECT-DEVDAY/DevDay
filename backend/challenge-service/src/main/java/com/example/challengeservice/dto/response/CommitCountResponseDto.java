package com.example.challengeservice.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommitCountResponseDto {
    int commitCount;
    public static CommitCountResponseDto from(int commitCount){
        return CommitCountResponseDto.builder()
                .commitCount(commitCount)
                .build();
    }
}
