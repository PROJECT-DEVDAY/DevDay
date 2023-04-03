package com.example.challengeservice.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeJoinRequestDto {

    /** 유저 ID **/
    private Long userId;

    /** 챌린지 Room ID **/
    private Long challengeRoomId;

    /** 유저 닉네임 **/

    private String nickname;
}
