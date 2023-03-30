package com.example.challengeservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class SolvedListResponseDto {
    private Long id;
    private List<String> solvedList;
    private int count;
    private String successDate;

    public static SolvedListResponseDto from(List<String> solvedList, int count){
        return SolvedListResponseDto.builder()
                .solvedList(solvedList)
                .count(count)
                .build();
    }

    public static SolvedListResponseDto from(Long id, List<String> solvedList, int count, String successDate){
        return SolvedListResponseDto.builder()
                .id(id)
                .solvedList(solvedList)
                .count(count)
                .successDate(successDate)
                .build();
    }

}
