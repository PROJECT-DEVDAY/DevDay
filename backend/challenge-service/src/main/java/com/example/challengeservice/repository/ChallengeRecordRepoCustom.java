package com.example.challengeservice.repository;

import com.example.challengeservice.dto.response.AlgoRecordResponseDto;
import com.example.challengeservice.dto.response.ChallengeRecordResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordDetailResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.UserChallenge;

import java.util.List;
import java.util.Optional;

public interface ChallengeRecordRepoCustom {

    List<PhotoRecordResponseDto> getSelfPhotoRecord(UserChallenge userChallenge,String viewType);
    List<PhotoRecordResponseDto> getTeamPhotoRecord(Long challengeRoomId,String viewType);
    Optional<AlgoRecordResponseDto> findByCreateAtAndUserChallenge(String createAt, UserChallenge userChallenge);
    Optional<ChallengeRecordResponseDto> findByUserChallengeIdAndCreateAt(Long userChallengeId, String createAt);
}
