package com.example.challengeservice.entity;


import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_RECORD_ID")
    private Long id;

    /** 신고한 유저아이디 **/
    @Column(nullable = false)
    private Long userId;

    /** 신고된 인증 기록 **/
    @ManyToOne(targetEntity = ChallengeRecord.class, fetch=FetchType.LAZY)
    @JoinColumn(name="CHALLENGE_RECORD_ID")
    private ChallengeRecord challengeRecord;


    /** 신고된 날짜 **/
    @Column(nullable = false)
    private String reportDate;

    public static ReportRecord from (Long userId , ChallengeRecord challengeRecord , String reportDate){
        return ReportRecord.builder().
                userId(userId)
                .challengeRecord(challengeRecord)
                .reportDate(reportDate)
                .build();
    }
}
