package com.example.payservice.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class SimpleChallengeInfosRequest implements Serializable {
    @Builder.Default
    private List<Long> challengeIdList = new ArrayList<>();
}
