package com.example.challengeservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class GithubListResponseDto {
    public int count;

    public static GithubListResponseDto from(List<String> solvedList, int count){
        return GithubListResponseDto.builder()
                .count(count)
                .build();
    }

}
