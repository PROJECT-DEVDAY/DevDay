package com.example.challengeservice.repository;

import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.UserChallenge;

import java.util.List;
import java.util.Optional;

public interface ChallengeRecordRepoCustom {

    List<PhotoRecordResponseDto> getSelfPhotoRecord(UserChallenge userChallenge, String viewType);



    Optional<ChallengeRecord> findByCreateAtAndUserChallenge(String createAt, UserChallenge userChallenge);

    List<ChallengeRecordResponseDto> findByUserChallengeIdAndCreateAt(Long userChallengeId, String createAt);

    List<RecordResponseDto> getTeamCommitRecord(Long challengeRoomId, String date);
    List<RecordResponseDto> getTeamAlgoRecord(Long challengeRoomId, String date);

    List<RecordResponseDto> getTeamPhotoRecord(Long challengeRoomId, String date);
}