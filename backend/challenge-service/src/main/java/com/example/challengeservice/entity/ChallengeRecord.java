package com.example.challengeservice.entity;


import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // 하위 테이블의 구분 컬럼 생성(default = DTYPE)

public class ChallengeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHALLENGE_RECORD_ID")
    private Long id;

    /** 기록 날짜 **/
    private Date createAt;

    /** 성공 여부 **/

    private boolean success;


    /** 챌린지 기록 ID **/
    private long UserChallenge;

    /** 신고 횟수 **/
    private int reportCount;

    /**  리더 신고 여부 **/
    private boolean hostReport;


    /** 최소 알고리즘 개수 **/
    private int algorithmCount;

    /** 최소 커밋 개수 **/

    private int commitCount;


    /** 인증 사진  url **/
    private String photoUrl;



}
