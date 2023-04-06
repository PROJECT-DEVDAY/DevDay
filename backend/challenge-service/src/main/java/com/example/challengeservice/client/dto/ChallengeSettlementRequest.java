package com.example.challengeservice.client.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter

public class ChallengeSettlementRequest {

    @JsonProperty("result")
   List<ChallengeSettleInfo> challengeSettleInfos ;


    public ChallengeSettlementRequest(List<ChallengeSettleInfo> challengeSettleInfos) {
        this.challengeSettleInfos = challengeSettleInfos;
    }
}
