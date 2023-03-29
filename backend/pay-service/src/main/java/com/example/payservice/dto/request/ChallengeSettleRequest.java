package com.example.payservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChallengeSettleRequest {

    @JsonProperty("result")
    List<ChallengeSettleInfo> resultList = new ArrayList<>();
    @Data
    public static class ChallengeSettleInfo {
        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("money")
        @Positive
        private Integer money;
    }
}
