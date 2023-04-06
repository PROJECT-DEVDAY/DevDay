package com.example.payservice.dto.challenge;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeJoinRequestDto {
	private Long userId;
	private Long challengeRoomId;
	private String nickname;
}
