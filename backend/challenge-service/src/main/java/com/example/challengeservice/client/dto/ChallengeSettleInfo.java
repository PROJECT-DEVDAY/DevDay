package com.example.challengeservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChallengeSettleInfo {

    /** 챌린지 방 ID **/
    @JsonProperty("challengeId")
    Long challengeRoomId;

    /** 유저 ID **/
    @JsonProperty("user_id")
    Long userId;

    /** 금액 **/
    @JsonProperty("money")
    Long diffPrice;

    public ChallengeSettleInfo(Long challengeRoomId, Long userId, Long diffPrice) {
        this.challengeRoomId = challengeRoomId;
        this.userId = userId;
        this.diffPrice = diffPrice;
    }
}
