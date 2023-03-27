package com.example.payservice.dto.challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleChallengeInfo {

    @JsonProperty("challenge_id")
    private long challengeId;

    private String title;
    private String startDate;
    private String endDate;

    public SimpleChallengeInfo(Long challengeId) {
        this.challengeId = challengeId;
    }
}
