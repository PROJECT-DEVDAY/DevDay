package com.example.payservice.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 종료된 챌린지 정산 v2용
 * challenge-service로부터 응답받는 객체입니다.
 */
@Data
public class ChallengeAllSettleRequest {

	@JsonProperty("result")
	List<ChallengeSettleInfo> resultList = new ArrayList<>();
}
