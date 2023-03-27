package com.example.payservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeJoinResponse {
	// 유저 아이디
	private long userId;
	// 챌린지 아이디
	private long challengeId;
	// 챌린지 참여 승인
	private boolean approve;
}
