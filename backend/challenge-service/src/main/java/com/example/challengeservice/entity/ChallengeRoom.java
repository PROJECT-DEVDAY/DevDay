package com.example.challengeservice.entity;


import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
    private String successUrl;

    /** 실패 인증 URL **/
    private String failUrl;

    /** 생성날짜 **/

    /** 인원수 **/
    @Column(nullable = false)
    private int userCount;

    /** 설명 **/
    @Column(nullable = false)
    private String introduce;

    /** 공개 / 비공개 여부(추후 추가) **/

    private boolean isOpen;

    /** 분류 **/
    private String category;

    /** 비밀번호 (추후 추가) **/

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

    /** 현재 참여자 수 **/
    private int participantsSize;


    /** 첼린지 이미지 **/
    @Column(columnDefinition = "VARCHAR(500) DEFAULT 'default_value'")
    private String backGroundUrl;


    public static ChallengeRoom from(ChallengeRoomRequestDto dto){
         return ChallengeRoom.builder()
                 .title(dto.getTitle())
                 .hostId(dto.getHostId())
                 .entryFee(dto.getEntryFee())
                 .userCount(dto.getUserCount())
                 .introduce(dto.getIntroduce())
                 .category(dto.getCategory())
                 .startDate(dto.getStartDate())
                 .endDate(dto.getEndDate())
                 .commitCount(dto.getCommitCount())
                 .algorithmCount(dto.getAlgorithmCount())
                 .build();
     }

     public void setCertificationUrl (String successUrl ,String failUrl){
        this.successUrl = successUrl;
        this.failUrl = failUrl;
     }

    public void setBackGroundUrl (String backGroundUrl){
        this.backGroundUrl = backGroundUrl;
    }


}
