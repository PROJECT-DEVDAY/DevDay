package com.example.challengeservice.dto.response;

import com.example.challengeservice.entity.UserChallenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationResponseDto {
    private String name;
    private String title;
    private String startDate;
    private String endDate;
    private String progressRate;
    private Long userChallengeId;

}
