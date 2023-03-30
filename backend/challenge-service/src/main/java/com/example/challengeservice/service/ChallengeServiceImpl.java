package com.example.challengeservice.service;

import com.example.challengeservice.client.UserServiceClient;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.request.ProblemRequestDto;
import com.example.challengeservice.dto.request.ReportRecordRequestDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.ReportRecord;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.infra.querydsl.SearchParam;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import feign.FeignException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeServiceImpl implements ChallengeService{
    private final UserServiceClient userServiceClient;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRoomRepository challengeRoomRepository;
    private final AmazonS3Service amazonS3Service;
    private final CommonServiceImpl commonService;
    private final ChallengeRecordRepository challengeRecordRepository;

    private final ReportRecordRepository reportRecordRepository;



    @Override
    public List<SimpleChallengeResponseDto> getListSimpleChallenge(String type, String search, int size, Long offset ) {

        //검색에 필요한 조건을 담은 객체
        SearchParam searchParam = new SearchParam(type,search,size,offset,commonService.getDate());

        List<ChallengeRoom> challengeRooms = challengeRoomRepository.getSimpleChallengeList(searchParam);

        for (ChallengeRoom challengeRoom : challengeRooms) {
            // 현재 참여자 수 조회
            log.info("가져온 챌린지 id"+ challengeRoom.getId());
            challengeRoom.setParticipantsSize(userChallengeRepository.countByChallengeRoomId(challengeRoom.getId()));
        }

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SimpleChallengeResponseDto>>() {}.getType(); // 리스트 타입 지정
        List<SimpleChallengeResponseDto> dtoList = modelMapper.map(challengeRooms, listType); // 변환

        return dtoList;
    }

    @Override
    public Long createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto)  throws IOException {

        String successUrl = "";
        String failUrl = "";
        String backgroundUrl = "";

        if(challengeRoomRequestDto.getType().equals("ALL")){
            //[예외 체크] 1. 자유 챌린지인 경우 , 인증 성공 , 실패에 대한 이미지 파일이 존재한지 판단한다.
            if(challengeRoomRequestDto.getCertSuccessFile()==null || challengeRoomRequestDto.getCertFailFile()==null)
                throw new ApiException(ExceptionEnum.CHALLENGE_BAD_REQUEST);


            //인증 성공,실패의 사진을 업로드
            successUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertSuccessFile(),"ChallengeRoom");
            failUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertFailFile(),"ChallengeRoom");

        }

         backgroundUrl = amazonS3Service.upload(challengeRoomRequestDto.getBackGroundFile(),"ChallengeRoom");

        ChallengeRoom challengeRoom = ChallengeRoom.from(challengeRoomRequestDto);

        if(challengeRoomRequestDto.getType().equals("ALL")) challengeRoom.setCertificationUrl(successUrl,failUrl);
        challengeRoom.setBackGroundUrl(backgroundUrl);


        Long challengeId = challengeRoomRepository.save(challengeRoom).getId();


        //챌린지 방이 잘 생성 되었다면 방을 만든 방장은 방에 참가해야한다.
        joinChallenge(challengeId,challengeRoomRequestDto.getHostId());


        return challengeId;
    }

    @Override
    public ChallengeRoomResponseDto readChallenge(Long challengeId){
        ChallengeRoom challengeRoom=challengeRoomRepository.findChallengeRoomById(challengeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));
        // 현재 참여자 수 조회
        challengeRoom.setParticipantsSize(userChallengeRepository.countByChallengeRoomId(challengeId));

        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(challengeRoom, ChallengeRoomResponseDto.class);
    }

    public Map<Long, ChallengeInfoResponseDto> challengeInfoList(List<Long> challengeIdList){
        Map<Long, ChallengeInfoResponseDto> challengeInfoResponseDtoMap=new HashMap<>();
        for(Long challengeId: challengeIdList){
            ChallengeRoom challengeRoom=challengeRoomRepository.findChallengeRoomById(challengeId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));
            ModelMapper mapper=new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            ChallengeInfoResponseDto challengeInfoResponseDto= mapper.map(challengeRoom, ChallengeInfoResponseDto.class);
            challengeInfoResponseDtoMap.put(challengeId, challengeInfoResponseDto);
        }
        return challengeInfoResponseDtoMap;
    }

    @Override
    @Transactional
    public Long joinChallenge(Long challengeId, Long userId) {
        // 해당 방 정보 가져오기
        ChallengeRoomResponseDto challengeRoomResponseDto = readChallenge(challengeId);
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChallengeRoom challengeRoom = mapper.map(challengeRoomResponseDto, ChallengeRoom.class);

        // userId 해당 challengeId에 이미 있는지 확인
        Optional<UserChallenge> checkUserChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoom.getId(), userId);
        if (checkUserChallenge.isPresent()) throw new ApiException(ExceptionEnum.USER_CHALLENGE_EXIST_EXCEPTION);

        // 없다면, 생성
        UserChallenge userChallenge = UserChallenge.from(challengeRoom, userId);
        userChallengeRepository.save(userChallenge);
        return userChallenge.getId();
    }

    @Override
    public UserChallengeInfoResponseDto myChallengeList(Long userId) {
        // 현재
        List<UserChallenge> userChallengingList = userChallengeRepository.findUserChallengingByUserId(userId, commonService.getDate());
        List<UserChallenge> userChallengedList = userChallengeRepository.findUserChallengedByUserId(userId, commonService.getDate());
        List<UserChallenge> userHostChallengesList = userChallengeRepository.findUserHostChallengesUserId(userId);

        System.out.println("userChallengingList = "+userChallengingList);
        System.out.println("userChallengedList = "+userChallengedList);
        System.out.println("userHostChallengesList = "+userHostChallengesList);
        UserChallengeInfoResponseDto userChallengeInfoResponseDto=UserChallengeInfoResponseDto.from(
                userChallengingList.size(),
                userChallengedList.size(),
                userHostChallengesList.size()
        );
        return userChallengeInfoResponseDto;
    }

    @Override
    public SolvedListResponseDto solvedProblemList(String baekjoonId) {
        String baekJoonUrl = "https://www.acmicpc.net/user/";
        baekJoonUrl+=baekjoonId;
        Connection conn = Jsoup.connect(baekJoonUrl);
        List<String> solvedList= new ArrayList<>();
        int count=0;
        try {
            Document document = conn.get();
            Elements imageUrlElements = document.getElementsByClass("problem-list");
            Element element = imageUrlElements.get(0);
            Elements problems= element.getElementsByTag("a");
            for (Element problem : problems) {
                solvedList.add(problem.text());
                count++;
            }
            System.out.printf("총 %d 문제 풀이하셨습니다\n", count);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SolvedListResponseDto solvedListResponseDto = SolvedListResponseDto.from(solvedList, count);
        return solvedListResponseDto;
    }

    /** 유저의 백준리스트 업데이트
     * Todo : 예외처리 추가
     * **/
    public void updateUserBaekjoon(Long userId){
        /* Feign Exception Handling */
        List<String> diffSolvedList=new ArrayList<>();
        try {
            SingleResult<BaekjoonListResponseDto> baekjoonListResponseDto = userServiceClient.getUserBaekjoonList(userId);
            String baekjoonId = baekjoonListResponseDto.getData().getBaekjoonId();
            Map<String, String> problemList = baekjoonListResponseDto.getData().getProblemList();
            List<String> newSolvedList=solvedProblemList(baekjoonId).getSolvedList();
            for(String s:newSolvedList){
                if(problemList.get(s)==null){
//                problemList.put(s, commonService.getDate());
                    diffSolvedList.add(s);
                }
            }
        } catch(FeignException ex){
            log.error(ex.getMessage());
        } catch(Exception e){
            e.printStackTrace();
        }
//        List<String> diffSolvedList=new ArrayList<>();
//        SingleResult<BaekjoonListResponseDto> baekjoonListResponseDto = userServiceClient.getUserBaekjoonList(userId);
//        String baekjoonId = baekjoonListResponseDto.getData().getBaekjoonId();
//        Map<String, String> problemList = baekjoonListResponseDto.getData().getProblemList();
//        List<String> newSolvedList=solvedProblemList(baekjoonId).getSolvedList();
//        for(String s:newSolvedList){
//            if(problemList.get(s)==null){
////                problemList.put(s, commonService.getDate());
//                diffSolvedList.add(s);
//            }
//        }
        if(diffSolvedList.size()>0)
            userServiceClient.createProblem(userId, ProblemRequestDto.from(diffSolvedList));
        return;
    };

    /** 신대득
     * 인증 정보 저장 (알고리즘)
     * 제작중
     * Todo : 오후 11시 50분에 이 작업을 스케줄러로 실행 시켜야함!!!
     *  **/
    @Override
    public void createAlgoRecord(ChallengeRecordRequestDto requestDto) throws IOException {
        log.info("챌린지id " + requestDto.getChallengeRoomId() + "유저아이디id" + requestDto.getUserId());
        ChallengeRoomResponseDto challengeRoom = readChallenge(requestDto.getChallengeRoomId());
        UserResponseDto user = userServiceClient.getUserInfo(requestDto.getUserId()).getData();

        //오늘 날짜
        String date = commonService.getDate();

        // Todo :user의 solved ac에서 오늘 푼 문제들만 조회하기!
        BaekjoonListResponseDto baekjoonListResponseDto= userServiceClient.getUserBaekjoonList(user.getUserId()).getData();

        // challengeRoom에서 최소 알고리즘 개수 가져오기
        // 인증 회수 미달이면 실패 Exception
        if(baekjoonListResponseDto.getProblemList().size()<challengeRoom.getAlgorithmCount()){
            throw new ApiException(ExceptionEnum.CONFIRM_FAILURE_ALGO_EXCEPTION);
        }

        // UserChallenge 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), requestDto.getUserId()).orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        log.info("[userChallenge id값]" + userChallenge.getId());
        ChallengeRecord challengeRecord = ChallengeRecord.fromAlgo(requestDto, date, baekjoonListResponseDto.getProblemList().size() , userChallenge);
        challengeRecordRepository.save(challengeRecord);
    }

    /**인증 정보 저장 (사진)**/
    @Override
    public void createPhotoRecord(ChallengeRecordRequestDto requestDto) throws IOException {

        log.info("챌린지id "+ requestDto.getChallengeRoomId()+"유저아이디id"+requestDto.getUserId());

        //인증 사진이 없는경우 예외처리
        if(requestDto.getPhotoCertFile()==null) throw new ApiException(ExceptionEnum.CHALLENGE_BAD_REQUEST);

        //인증 사진이 정상적으로 온 경우 사진을 s3에 업로드한다.
        String photoUrl = amazonS3Service.upload(requestDto.getPhotoCertFile(),"CertificationPhoto");

        //오늘 날짜
        String date = commonService.getDate();

        // UserChallenge 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), requestDto.getUserId()).orElseThrow(()-> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION) );
        log.info("[userChallenge id값]"+ userChallenge.getId());
        ChallengeRecord challengeRecord = ChallengeRecord.from(requestDto,date,photoUrl,userChallenge);

        challengeRecordRepository.save(challengeRecord);
    }

    /** 사진 인증 개인 조회ㅏ  **/
    @Override
    public List<PhotoRecordResponseDto> getSelfPhotoRecord(Long challengeRoomId, Long userId, String viewType) {

        //userChallenge 값을 찾아야함

      UserChallenge userChallenge =  userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoomId , userId).orElseThrow(()->new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        List<PhotoRecordResponseDto> challengeRecords = challengeRecordRepository.getSelfPhotoRecord(userChallenge, viewType );
        return challengeRecords;
    }

    /**
     * 신대득
     * category에 따라 record를 만드는 메서드
     * 제작중!!
     * @param challengeRoomId
     * @param userId
     * @param viewType
     * @param category
     * @return
     */
    public List<?> getSelfRecord(Long challengeRoomId, Long userId, String viewType, String category) {
        List<?> selfRecord=new ArrayList<>();
        switch(category){
            case "ALGO":
                break;
            case "COMMIT":
                break;
            case "FREE":
                //userChallenge 값을 찾아야함
                selfRecord=getSelfPhotoRecord(challengeRoomId,userId,viewType);
                break;
        }
        return selfRecord;
    }
    @Override
    public List<PhotoRecordResponseDto> getTeamPhotoRecord(Long challengeRoomId, String viewType) {
        List<PhotoRecordResponseDto> challengeRecords = challengeRecordRepository.getTeamPhotoRecord(challengeRoomId, viewType );
        return challengeRecords;

    }


    /** 사진 인증 상세 조회 **/
    public PhotoRecordDetailResponseDto getPhotoRecordDetail(Long challengeRecordId){

        //해당 ChallengeRecord 찾기
        ChallengeRecord challengeRecord = challengeRecordRepository.findById(challengeRecordId).orElseThrow(()->new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));

        //유저 아이디 찾아와서 user-service 사용자 닉네임인증정보 요청하기

        Long userId =challengeRecord.getUserChallenge().getUserId();
        log.info("유저 아이디 누구인가요"+userId);

        UserResponseDto userResponseDto = userServiceClient.getUserInfo(userId).getData();
        log.info("유저정보 가져왔나요"+userResponseDto.getNickname());

        //사진 인증 상세 dto 생성
        PhotoRecordDetailResponseDto responseDto = new PhotoRecordDetailResponseDto(challengeRecord,userResponseDto.getNickname());

        return responseDto;
    }


    /** 사진 인증 신고하기 **/
    @Transactional
    @Override
    public void reportRecord(ReportRecordRequestDto reportRecordRequestDto) {

        Long userId = reportRecordRequestDto.getUserId();
        Long recordId = reportRecordRequestDto.getChallengeRecordId();
        Long challengeRoomId = reportRecordRequestDto.getChallengeRoomId();
        String reportDate = reportRecordRequestDto.getReportDate();


        //TODO  1. 사진인증 기록이 존재한다면 중복에러 발생
        if(reportRecordRepository.existsByUserIdAndChallengeRecordId(userId,recordId)) {
            throw new ApiException(ExceptionEnum.NOT_EXIST_REPORT_RECORD);
        }

        ChallengeRecord challengeRecord = challengeRecordRepository.findById(recordId).orElseThrow(()->new ApiException(ExceptionEnum.NOT_EXIST_CHALLENGE_RECORD));

        //TODO 1-1 신고하는 날짜가 인증날짜와 다를경우 신고할 수 없다. 예외를 발생시킨다.
        if(!challengeRecord.getCreateAt().equals(reportDate)){
            throw  new ApiException(ExceptionEnum.CHALLENGE_RECORD_BAD_REQUEST);
        }

        //TODO 1-2 자기가 자기 자신의 인증 기록을 신고할 수는 없다.

        if(challengeRecord.getUserChallenge().getUserId() == userId){
            throw new ApiException(ExceptionEnum.CHALLENGE_RECORD_SELF_REPORT);
        }


        //TODO  2. 위의 인증 기록 에외처리를 넘어가면 사진 신고 기록이 없다면 신고기록을 저장한다.
        reportRecordRepository.save(ReportRecord.from(userId,challengeRecord,reportDate));


        //TODO  Before 2. 기\ 해당 유저가 방장인지 아닌지 체크해야한다.
        ChallengeRoom challengeRoom = challengeRoomRepository.findById(challengeRoomId).orElseThrow(
               ()-> new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));


        //TODO  2-1 if) 저장하려고 하는 사람이 방장일 경우 방장의 신고로 인정한다.
        if(challengeRoom.getHostId() == userId)challengeRecord.doHostReport(); //true 로 변경 ->  방장이 신고를 함
        // TODO  2-2 if) 저장하려고 하는 사람이 방장이 아닐 경우 신고받은 횟수 +1을 한다.
        else challengeRecord.plusReportCount();

        //인증 실패의 기준은 참여자 수의 절반 이상이다. 홀수인경우는 과반수를 넘기기 위해 나머지가 존재하면 +1을 해준다.
        int participants = challengeRoom.getUserCount();
        int reportStandard = participants % 2 != 0 ? (participants / 2) + 1 : participants / 2;

        //TODO  3. 만약 신고기록 저장후에 해당 사진인증이 방 인원 전체의 절반 과 방장의 신고를 받은 기록이 있다면 해당 인증 기록은 실패로 처리한다.
        if(reportStandard >= challengeRecord.getReportCount() && challengeRecord.isHostReport() ) challengeRecord.setSuccessFail();



    }
}
