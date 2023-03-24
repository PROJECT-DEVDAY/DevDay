package com.example.challengeservice.dto.response;

import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ChallengeInfoResponseDto {
    private String title;
    private String startDate;
    private String endDate;
}