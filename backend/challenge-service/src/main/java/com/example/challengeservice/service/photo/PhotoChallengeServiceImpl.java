package com.example.challengeservice.service.photo;

import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ReportRecordRequestDto;
import com.example.challengeservice.dto.response.PhotoRecordDetailResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
import com.example.challengeservice.dto.response.RecordResponseDto;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.ReportRecord;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.repository.ChallengeRecordRepository;
import com.example.challengeservice.repository.ChallengeRoomRepository;
import com.example.challengeservice.repository.ReportRecordRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import com.example.challengeservice.service.common.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoChallengeServiceImpl implements PhotoChallengeService {

    private final AmazonS3Service amazonS3Service;
    private final CommonServiceImpl commonService;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ReportRecordRepository reportRecordRepository;
    private final ChallengeRoomRepository challengeRoomRepository;

    /** 사진 인증 기록 생성하기 **/
    @Override
    public void createPhotoRecord(Long userId , ChallengeRecordRequestDto requestDto) throws IOException {

        //인증 사진이 없는경우 예외처리
        if(requestDto.getPhotoCertFile()==null) throw new ApiException(ExceptionEnum.CHALLENGE_BAD_REQUEST);

        String date = commonService.getDate();
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), userId).orElseThrow(()-> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION) );

        String photoUrl = amazonS3Service.upload(requestDto.getPhotoCertFile(),"CertificationPhoto");
        ChallengeRecord challengeRecord = ChallengeRecord.from(date,photoUrl,userChallenge);

        challengeRecordRepository.save(challengeRecord);
    }



    /** 사진 인증 개인 조회  **/
    @Override
    public List<PhotoRecordResponseDto> getSelfPhotoRecord(Long challengeRoomId, Long userId, String viewType) {

        UserChallenge userChallenge =  userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoomId , userId).orElseThrow(()->new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        return challengeRecordRepository.getSelfPhotoRecord(userChallenge, viewType );
    }


    /**같은 방 챌린지 참여자의 인증 내역 조회 **/
    @Override
    public List<RecordResponseDto> getTeamPhotoRecord(Long userId , Long challengeRoomId, String date) {

        if(!userChallengeRepository.existsByChallengeRoomIdAndUserId(challengeRoomId , userId)) throw new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION);
        return challengeRecordRepository.getTeamPhotoRecord(challengeRoomId ,date);

    }

    /**  사진인증 기록을 상세 조회한다 **/
    public PhotoRecordDetailResponseDto getPhotoRecordDetail(Long userId, Long challengeRecordId){

        //[예외처리]: 조회하려는 인증기록이 존재하는지 확인한다.
        ChallengeRecord challengeRecord = challengeRecordRepository.findById(challengeRecordId).orElseThrow(()->new ApiException(ExceptionEnum.NOT_EXIST_CHALLENGE_RECORD));
        String writerNickname =challengeRecord.getUserChallenge().getNickname();

        //인증 기록을 조회하는 사용자의 신고 기록을 리턴해야한다. 존재하면 true를 그렇지 않으면 false를 리턴한다.
        boolean reportStatus = reportRecordRepository.existsByUserIdAndChallengeRecordId(userId, challengeRecordId);

        return new PhotoRecordDetailResponseDto(challengeRecord,writerNickname,reportStatus);
    }


    /** 사진 인증 신고하기  **/
    @Transactional
    @Override
    public void reportRecord(Long userId ,ReportRecordRequestDto reportRecordRequestDto) {

        Long recordId = reportRecordRequestDto.getChallengeRecordId();
        Long challengeRoomId = reportRecordRequestDto.getChallengeRoomId();
        String reportDate = reportRecordRequestDto.getReportDate();


        // [예외처리] 사진인증 기록이 존재한다면 중복에러 발생
        if(reportRecordRepository.existsByUserIdAndChallengeRecordId(userId,recordId)) {
            throw new ApiException(ExceptionEnum.NOT_EXIST_REPORT_RECORD);
        }

        ChallengeRecord challengeRecord = challengeRecordRepository.findById(recordId).orElseThrow(()->new ApiException(ExceptionEnum.NOT_EXIST_CHALLENGE_RECORD));

        // [예외처리] 신고하는 날짜가 인증날짜와 다를경우 신고할 수 없다. 예외를 발생시킨다.
        if(!challengeRecord.getCreateAt().equals(reportDate)){
            throw  new ApiException(ExceptionEnum.CHALLENGE_RECORD_BAD_REQUEST);
        }

        // [예외처리] 자기가 자기 자신의 인증 기록을 신고할 수는 없다.

        if(challengeRecord.getUserChallenge().getUserId() == userId){
            throw new ApiException(ExceptionEnum.CHALLENGE_RECORD_SELF_REPORT);
        }

        reportRecordRepository.save(ReportRecord.from(userId,challengeRecord,reportDate));


        //[예외처리]해당 유저가 방장인지 아닌지 체크해야한다.
        ChallengeRoom challengeRoom = challengeRoomRepository.findById(challengeRoomId).orElseThrow(
                ()-> new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));


        if(challengeRoom.getHostId() == userId)challengeRecord.doHostReport();
        else challengeRecord.plusReportCount();


        int participants = challengeRoom.getCurParticipantsSize();
        int reportStandard = participants % 2 != 0 ? (participants / 2) + 1 : participants / 2;

        if(reportStandard >= challengeRecord.getReportCount() && challengeRecord.isHostReport() ) challengeRecord.setSuccessFail();

    }


}
