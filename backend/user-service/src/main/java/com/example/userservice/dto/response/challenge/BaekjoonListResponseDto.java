package com.example.userservice.dto.response.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaekjoonListResponseDto {

    private String baekjoonId;

    private HashMap<String, String> problemList;

    public static BaekjoonListResponseDto of (String baekjoonId, HashMap<String, String> problemList) {
        return BaekjoonListResponseDto.builder()
                .baekjoonId(baekjoonId)
                .problemList(problemList)
                .build();
    }
}
