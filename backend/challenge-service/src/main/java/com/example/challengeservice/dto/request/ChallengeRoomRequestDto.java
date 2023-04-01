package com.example.challengeservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@RequiredArgsConstructor
public class ChallengeRoomRequestDto {


    /** 제목 **/
    @NotBlank(message = "제목을 반드시 입력해 주세요")
    @Size(min = 5, message = "제목은 5자 이상으로 입력해 주세요")
    private String title;

    /** 방장 ID **/
    private Long hostId;

    /** 참가비용 **/
    @Min( value = 1000, message = "참가 비용은 1,000원 이상부터 가능합니다.")
    private int entryFee;


    /** 최대 참가 인원 수 **/
    @Min( value = 1, message = "최소 1명 이상의 참가 인원이 필요합니다.")
    private int maxParticipantsSize;

    /** 설명 **/
    @NotBlank(message = "챌린지에 대한 소개글을 을 적어주세요.")
    @Size(min = 10, message = "소개글은 10자 이상 입력해주세요.")
    private String introduce;

    /** 챌린지 분류 **/
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;

    /** 시작기간 **/
    @NotBlank(message = "시작날짜를 입력해주세요.")
    private String startDate;

    /** 종료 기간 **/
    @NotBlank(message = "종료날짜를 입력해주세요.")
    private String endDate;

    /** 최소 커밋 수 **/
    private int commitCount;
    /** 최소 알고리즘 수 **/
    private int algorithmCount;

    /** 성공 인증 이미지 파일 **/
    private MultipartFile certSuccessFile;

    /** 실패 인증 이미지 파일 **/
    private MultipartFile certFailFile;

    /** 첼린지 이미지 **/
    private MultipartFile backGroundFile;
}
