package com.example.payservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChallengeSettleRequest {

    @JsonProperty("result")
    List<ChallengeSettleInfo> resultList = new ArrayList<>();
}
