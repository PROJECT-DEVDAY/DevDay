package com.example.userservice.dto.response;

import com.example.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypageResponseDto {

    private String profileImg;

    private String nickname;

    private Integer prize;

    private Integer deposit;

    private Integer challengingCnt;

    private Integer challengedCnt;

    private Integer leaderCnt;

    public static MypageResponseDto of (User user, ChallengeResponseDto challengeResponseDto, MoneyResponseDto moneyResponseDto) {
        return MypageResponseDto.builder()
                .profileImg(user.getProfileImgUrl())
                .nickname(user.getNickname())
                .prize(moneyResponseDto.getPrize())
                .deposit(moneyResponseDto.getDeposit())
                .challengingCnt(challengeResponseDto.getChallengingCnt())
                .challengedCnt(challengeResponseDto.getChallengedCnt())
                .leaderCnt(challengeResponseDto.getLeaderCnt())
                .build();
    }
}
