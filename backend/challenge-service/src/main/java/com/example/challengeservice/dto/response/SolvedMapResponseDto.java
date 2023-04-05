package com.example.challengeservice.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SolvedMapResponseDto {
    Map<String, List<String>> solvedMap;

    Map<String, Integer> commitMap;

    public static SolvedMapResponseDto algoFrom(Map<String, List<String>> solvedMap){
        return SolvedMapResponseDto.builder()
                .solvedMap(solvedMap)
                .build();
    }

    public static SolvedMapResponseDto commitFrom(Map<String, Integer> commitMap){
        return SolvedMapResponseDto.builder()
                .commitMap(commitMap)
                .build();
    }
}
