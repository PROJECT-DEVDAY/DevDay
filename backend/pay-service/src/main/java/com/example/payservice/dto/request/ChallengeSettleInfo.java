package com.example.payservice.dto.request;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChallengeSettleInfo {
	@JsonProperty("user_id")
	private Long userId;

	@JsonProperty("money")
	@Positive
	private Integer money;

	@JsonProperty("challengeId")
	private Long challengeId;
}
