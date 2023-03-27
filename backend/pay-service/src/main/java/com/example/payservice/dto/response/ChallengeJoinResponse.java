package com.example.payservice.dto.response;

import lombok.Data;

@Data
public class ChallengeJoinResponse {
	private long userId;
	private long challengeId;
	private boolean join;
}
