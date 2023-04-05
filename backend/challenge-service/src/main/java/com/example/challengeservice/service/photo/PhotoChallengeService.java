package com.example.challengeservice.service.photo;

import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ReportRecordRequestDto;
import com.example.challengeservice.dto.response.PhotoRecordDetailResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordResponseDto;

import java.io.IOException;
import java.util.List;

public interface PhotoChallengeService {

    /** 사진 인증 기록 생성하기 **/
    void createPhotoRecord (Long userId , ChallengeRecordRequestDto requestDto) throws IOException;

    /** 개인 사진 인증 기록 가져오기 **/
    List<PhotoRecordResponseDto> getSelfPhotoRecord(Long challengeId , Long userId, String viewType );

    /** 챌린지 참여자 팀원 사진 인증 기록 가져오기 **/
    List<PhotoRecordResponseDto> getTeamPhotoRecord(Long userId ,Long challengeRoomId , String date);
    /** 사진 인증 상세 보기 **/

    PhotoRecordDetailResponseDto getPhotoRecordDetail(Long userId , Long challengeRecordId);

    /** 사진 기록 신고하기 **/
    void reportRecord(Long userId ,ReportRecordRequestDto reportRecordRequestDto);
}
