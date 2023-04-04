package com.example.challengeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlgoRankResponseDto implements Comparable<AlgoRankResponseDto> {
    private Long rank;
    private Long userId;
    private String userNickname;
    private Long successCount;
    private Long failCount;

    @Override
    public int compareTo(@NotNull AlgoRankResponseDto o) {
        return o.successCount.intValue()-this.successCount.intValue(); //내림차순
    }
    public void setRank(Long rank){
        this.rank=rank;
    }
}
