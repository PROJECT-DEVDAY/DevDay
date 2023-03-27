package com.example.challengeservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class SolvedListResponseDto {
    public List<String> solvedList;
    public int count;

    public static SolvedListResponseDto from(List<String> solvedList, int count){
        return SolvedListResponseDto.builder()
                .solvedList(solvedList)
                .count(count)
                .build();
    }

}
