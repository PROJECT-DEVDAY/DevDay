package com.example.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeResponseDto {

    private Integer challengingCnt;

    private Integer challengedCnt;

    private Integer leaderCnt;
}
