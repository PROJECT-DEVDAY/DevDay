package com.example.challengeservice.entity;


import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter

public class ChallengeRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHALLENGE_ID")
    private Long id;


    /** 제목 **/
    @Column(nullable = false, length = 100)
    private String title;

    /** 방장ID **/
    @Column(nullable = false)
    private Long host;

    /** 참가비용 **/
    @Column(nullable = false)
    private int entryFee;

    /** 성공인증 URL **/
    private String successUrl;

    /** 실패 인증 URL **/
    private String failUrl;

    /** 생성날짜 **/

    /** 인원수 **/
    @Column(nullable = false)
    private int userCount;

    /** 설명 **/
    @Column(nullable = false)
    private String desc;

    /** 공개 / 비공개 여부(추후 추가) **/

    private boolean isOpen;

    /** 분류 **/

    private String type;

    /** 비밀번호 (추후 추가) **/

    /** 시작기간 **/
    @Column(nullable = false)
    private Date startDate;

    /** 종료 기간 **/
    @Column(nullable = false)
    private Date endDate;

    /** 상태 **/

    private String status;

    /** 최소 커밋 수 **/
    @Column(nullable = true)
    private int commitCount;

    /** 최소 알고리즘 문제 수 **/
    @Column(nullable = true)
    private int algorithmCount;


    /** 첼린지 이미지 **/
    @Column(nullable = false, length = 100)
    private String backGroundUrl;








}
