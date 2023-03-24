package com.example.challengeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChallengeInfoResponseDto {
    private Integer challengingCnt;
    private Integer challengedCnt;
    private Integer leaderCnt;

    public static UserChallengeInfoResponseDto from(int challengingCnt, int challengedCnt, int leaderCnt){
        return UserChallengeInfoResponseDto.builder()
                .challengingCnt(challengingCnt)
                .challengedCnt(challengedCnt)
                .leaderCnt(leaderCnt)
                .build();
    };
}
