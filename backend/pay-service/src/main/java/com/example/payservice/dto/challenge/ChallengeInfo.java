package com.example.payservice.dto.challenge;

import lombok.Data;

@Data
public class ChallengeInfo {

    // 챌린지 ID
    private Long id;

    // 챌린지 타입
    private String type;

    // 챌린지 제목
    private String title;

    // 챌린지 시작일
    private String startDate;

    // 챌린지 종료일
    private String endDate;
}
