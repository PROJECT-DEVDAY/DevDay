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
}
