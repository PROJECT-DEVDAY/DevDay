package com.example.challengeservice.entity;


import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHALLENGE_ROOM_ID")
    private Long id;


    /** 제목 **/
    @Column(nullable = false, length = 100)
    private String title;

    /** 방장ID **/
    @Column(nullable = false)
    private Long hostId;

    /** 참가비용 **/
    @Column(nullable = false)
    private int entryFee;

    /** 성공인증 URL **/
    @Column(columnDefinition = "VARCHAR(500)")
    private String successUrl;

    /** 실패 인증 URL **/
    @Column(columnDefinition = "VARCHAR(500)")
    private String failUrl;


    /** 설명 **/
    @Column(nullable = false)
    private String introduce;


    private boolean isOpen;

    /** 분류 **/
    private String category;

    /** 시작기간 **/
    @Column(nullable = false)
    private String startDate;

    /** 종료 기간 **/
    @Column(nullable = false)
    private String endDate;

    /** 상태 **/
    private String status;

    /** 최소 커밋 수 **/
    private int commitCount;

    /** 최소 알고리즘 문제 수 **/
    private int algorithmCount;

    /** 첼린지 이미지 **/
    @Column(columnDefinition = "VARCHAR(500) DEFAULT 'default_value'")
    private String backGroundUrl;

    /** 현재 참여자 수 **/
    @Column(name = "CUR_NUMBER_PARTICIPANTS")
    private int curParticipantsSize;


    /** 최대 참여자 수 **/
    @Column(name = "MAXIMUM_NUMBER_PARTICIPANTS")
    private int maxParticipantsSize;

    public static ChallengeRoom from(ChallengeRoomRequestDto dto){
         return ChallengeRoom.builder()
                 .title(dto.getTitle())
                 .hostId(dto.getHostId())
                 .entryFee(dto.getEntryFee())
                 .maxParticipantsSize(dto.getMaxParticipantsSize())
                 .introduce(dto.getIntroduce())
                 .category(dto.getCategory())
                 .startDate(dto.getStartDate())
                 .endDate(dto.getEndDate())
                 .commitCount(dto.getCommitCount())
                 .algorithmCount(dto.getAlgorithmCount())
                 .build();
     }

     public void setCertificationUrl (String successUrl,String failUrl){
        this.successUrl = successUrl;
        this.failUrl = failUrl;
     }

    public void setBackGroundUrl (String backGroundUrl){
        this.backGroundUrl = backGroundUrl;
    }

    public void plusCurParticipantsSize (){
        this.curParticipantsSize++;
    }

}
