package com.example.userservice.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubBaekjoonResponseDto {

    private String github;

    private String baekjoon;

    public static GithubBaekjoonResponseDto from (String github, String baekjoon) {
        return GithubBaekjoonResponseDto.builder()
                .github(github)
                .baekjoon(baekjoon)
                .build();
    }
}
