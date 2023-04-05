package com.example.challengeservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class CommitListResponseDto {
    private Long id;
    private int count;
    private String successDate;

    public static CommitListResponseDto from(Long id, int count, String successDate){
        return CommitListResponseDto.builder()
                .id(id)
                .count(count)
                .successDate(successDate)
                .build();
    }

}
