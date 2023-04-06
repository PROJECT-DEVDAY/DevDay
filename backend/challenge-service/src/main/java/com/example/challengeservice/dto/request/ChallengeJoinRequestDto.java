package com.example.challengeservice.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeJoinRequestDto {

    /** 유저 ID **/
    private Long userId;
    @NotNull("참가할 챌린지 방 ID를 입력해주세요.")
    private Long challengeRoomId;

    @NotBlank (message = "참가할 유저의 닉네임을 입력해주세요.")
    private String nickname;
}
