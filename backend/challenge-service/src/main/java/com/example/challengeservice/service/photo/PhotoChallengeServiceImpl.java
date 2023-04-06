package com.example.challengeservice.service.photo;

import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ReportRecordRequestDto;
import com.example.challengeservice.dto.response.PhotoRecordDetailResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
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
import com.example.challengeservice.service.CommonServiceImpl;
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

        //오늘 날짜
        String date = commonService.getDate();

        //[예외처리] 당일에 인증했던 기록이 있다면 기록할 수 없음


        // UserChallenge 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), userId).orElseThrow(()-> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION) );

        //인증 사진이 정상적으로 온 경우 사진을 s3에 업로드한다.
        String photoUrl = amazonS3Service.upload(requestDto.getPhotoCertFile(),"CertificationPhoto");
        ChallengeRecord challengeRecord = ChallengeRecord.from(date,photoUrl,userChallenge);

        challengeRecordRepository.save(challengeRecord);
    }



    /** 사진 인증 개인 조회  **/
    @Override
    public List<PhotoRecordResponseDto> getSelfPhotoRecord(Long challengeRoomId, Long userId, String viewType) {

        //userChallenge 값을 찾아야함

        UserChallenge userChallenge =  userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoomId , userId).orElseThrow(()->new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        return challengeRecordRepository.getSelfPhotoRecord(userChallenge, viewType );
    }


    /**같은 방 챌린지 참여자의 인증 내역 조회 **/
    @Override
    public List<PhotoRecordResponseDto> getTeamPhotoRecord(Long userId ,Long challengeRoomId, String date) {

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


    /** 사진 인증 신고하기 (로그인, 권한 인증) **/
    @Transactional
    @Override
    public void reportRecord(Long userId ,ReportRecordRequestDto reportRecordRequestDto) {

        Long recordId = reportRecordRequestDto.getChallengeRecordId();
        Long challengeRoomId = reportRecordRequestDto.getChallengeRoomId();
        String reportDate = reportRecordRequestDto.getReportDate();


        // 1. 사진인증 기록이 존재한다면 중복에러 발생
        if(reportRecordRepository.existsByUserIdAndChallengeRecordId(userId,recordId)) {
            throw new ApiException(ExceptionEnum.NOT_EXIST_REPORT_RECORD);
        }

        ChallengeRecord challengeRecord = challengeRecordRepository.findById(recordId).orElseThrow(()->new ApiException(ExceptionEnum.NOT_EXIST_CHALLENGE_RECORD));

        // 1-1 신고하는 날짜가 인증날짜와 다를경우 신고할 수 없다. 예외를 발생시킨다.
        if(!challengeRecord.getCreateAt().equals(reportDate)){
            throw  new ApiException(ExceptionEnum.CHALLENGE_RECORD_BAD_REQUEST);
        }

        // 1-2 자기가 자기 자신의 인증 기록을 신고할 수는 없다.

        if(challengeRecord.getUserChallenge().getUserId() == userId){
            throw new ApiException(ExceptionEnum.CHALLENGE_RECORD_SELF_REPORT);
        }


        //  2. 위의 인증 기록 에외처리를 넘어가면 사진 신고 기록이 없다면 신고기록을 저장한다.
        reportRecordRepository.save(ReportRecord.from(userId,challengeRecord,reportDate));


        //  Before 2. 기\ 해당 유저가 방장인지 아닌지 체크해야한다.
        ChallengeRoom challengeRoom = challengeRoomRepository.findById(challengeRoomId).orElseThrow(
                ()-> new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));


        //  2-1 if) 저장하려고 하는 사람이 방장일 경우 방장의 신고로 인정한다.
        if(challengeRoom.getHostId() == userId)challengeRecord.doHostReport(); //true 로 변경 ->  방장이 신고를 함
            //  2-2 if) 저장하려고 하는 사람이 방장이 아닐 경우 신고받은 횟수 +1을 한다.
        else challengeRecord.plusReportCount();

        //인증 실패의 기준은 참여자 수의 절반 이상이다. 홀수인경우는 과반수를 넘기기 위해 나머지가 존재하면 +1을 해준다.
        int participants = challengeRoom.getCurParticipantsSize();
        int reportStandard = participants % 2 != 0 ? (participants / 2) + 1 : participants / 2;

        //  3. 만약 신고기록 저장후에 해당 사진인증이 방 인원 전체의 절반 과 방장의 신고를 받은 기록이 있다면 해당 인증 기록은 실패로 처리한다.
        if(reportStandard >= challengeRecord.getReportCount() && challengeRecord.isHostReport() ) challengeRecord.setSuccessFail();

    }


}
