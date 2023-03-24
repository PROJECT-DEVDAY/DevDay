package com.example.challengeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaekjoonListResponseDto {

    private String baekjoonId;

    private Map<String, String> problemList;
}
