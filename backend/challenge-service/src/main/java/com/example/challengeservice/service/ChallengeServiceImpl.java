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
import feign.FeignException;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.core.env.Environment;
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

    private final Environment env;




    /**
     * author : 홍금비
     * explain : 메인페이지에서 검색 조건에 따른 첼린지 조회
     * @param category 챌린지 종류
     * @param search 검색어
     * @param size 검색될 개수
     * @param offset 마지막으로 검색된 challengeRoomId
     * **/
    @Override
    public List<SimpleChallengeResponseDto> getListSimpleChallenge(String category ,String search,int size,Long offset ) {

        //searchParam :검색에 필요한 조건을 담은 객체
        SearchParam searchParam = new SearchParam(category,search,size,offset,commonService.getDate());

        List<ChallengeRoom> challengeRooms = challengeRoomRepository.getSimpleChallengeList(searchParam);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SimpleChallengeResponseDto>>() {}.getType(); // 리스트 타입 지정
        List<SimpleChallengeResponseDto> dtoList = modelMapper.map(challengeRooms, listType); // 변환

        return dtoList;
    }

    /**
     * author : 홍금비
     * explain : ChallengeRoom 생성
     * @param challengeRoomRequestDto ChallengeRoom를 생성하는데 필요한 필드 값
     * @return 생성된 ChallengeRoomID
     * @throws IOException
     * retouch : 생성된 ChallengeRoom을 save하기전에 S3에 이미지를 업로드 하는것이 아니라 save가 되면 그 때 이미지를 업로드 할 수 있도록 변경
     */

    @Transactional
    @Override
    public Long createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto)  throws IOException {



        String successUrl = "";
        String failUrl = "";
        String backgroundUrl = "";

        //TODO [예외 체크] 1. 자유 챌린지인 경우 , 인증 성공 , 실패에 대한 이미지 파일이 존재한지 판단한다.
        if(challengeRoomRequestDto.getCategory().equals("FREE")){
            if(challengeRoomRequestDto.getCertSuccessFile()==null || challengeRoomRequestDto.getCertFailFile()==null)
                throw new ApiException(ExceptionEnum.CHALLENGE_BAD_REQUEST);
        }

        ChallengeRoom challengeRoom = ChallengeRoom.from(challengeRoomRequestDto);

        Long challengeId = challengeRoomRepository.save(challengeRoom).getId();



            if(challengeRoomRequestDto.getBackGroundFile().isEmpty()) {

                switch (challengeRoom.getCategory()) {

                    case "FREE":
                        challengeRoom.setBackGroundUrl(env.getProperty("default-image.free"));
                        break;
                    case "ALGO":
                        challengeRoom.setBackGroundUrl(env.getProperty("default-image.algo"));
                        break;
                    case "COMMIT":
                        challengeRoom.setBackGroundUrl(env.getProperty("default-image.commit"));
                        break;
                }
            }else{
                    backgroundUrl = amazonS3Service.upload(challengeRoomRequestDto.getBackGroundFile(),"ChallengeRoom");
                    challengeRoom.setBackGroundUrl(backgroundUrl);
                }



        if(challengeRoomRequestDto.getCategory().equals("FREE")) {
            //인증 성공,실패의 사진을 업로드
            successUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertSuccessFile(),"ChallengeRoom");
            failUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertFailFile(),"ChallengeRoom");
            challengeRoom.setCertificationUrl(successUrl,failUrl);
        }

        //챌린지 방이 잘 생성 되었다면 방을 만든 방장은 방에 참가해야한다.
        joinChallenge(challengeId,challengeRoomRequestDto.getHostId());

        return challengeId;
    }

    /**
     * author  : 신대득
     * explain : 챌린지방 정보 조회
     * @param challengeId ChallengeRoomId
     * @return ChallengeRoomResponseDto로 리턴
     */
    @Override
    public ChallengeRoomResponseDto readChallenge(Long challengeId){

        ChallengeRoom challengeRoom= getChallengeRoomEntity(challengeId);

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

    /***
     * author  : 신대득
     * explain : 해당 유저의 챌린지 참가정보를 저장한다.
     * @param challengeRoomId 챌린지방 ID
     * @param userId 유저 ID
     * @return
     * retouch : find 대신 exist 함수로 존재 유무 확인  Optional<UserChallenge> -> UserChallenge orElseThrow로 변경
     *           해당 챌린지에 참여가능한 인원 수가 넘으면 참가할 수 없도록 예외처리 작성
     */

    @Override
    @Transactional
    public boolean joinChallenge(Long challengeRoomId, Long userId) {

        //해당 challengeRoom Entity 조회
        ChallengeRoom challengeRoom = getChallengeRoomEntity(challengeRoomId);

        // 예외 처리 :참여 인원이 전부 찬 경우는 해당 챌린지에 참여할 수 없음
        if (challengeRoom.getCurParticipantsSize() == challengeRoom.getMaxParticipantsSize() ) {
            throw  new ApiException(ExceptionEnum.UNABLE_TO_JOIN_CHALLENGEROOM);
        }
        //해당 userId가 챌린지방에 참여했는지 확인
        boolean isJoinUser  = userChallengeRepository.existsByChallengeRoomIdAndUserId(challengeRoomId, userId);

        //참가한 기록이 없다면 userChallenge(참가자 목록) 에 저장하고 challengeRoom 의 현재 참가 인원을 +1 한다.

        if(!isJoinUser) {
            UserChallenge userChallenge = UserChallenge.from(challengeRoom, userId);
            userChallengeRepository.save(userChallenge);
            challengeRoom.plusCurParticipantsSize(); // +1 증가
        }

        return isJoinUser;
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

    /**
     * 신대득
     * githubId로 commit 정보들을 가져오는 메서드
     * @param githubId
     */
    @Override
    public int getGithubCommit(String githubId){
        String githubUrl = "https://github.com/";
        githubUrl+=githubId;
        Connection conn = Jsoup.connect(githubUrl);
        List<String> solvedList= new ArrayList<>();
        int count=0;
        try {
            Document document = conn.get();
            Elements imageUrlElements = document.getElementsByClass("js-calendar-graph-svg");
            Element element = imageUrlElements.get(0);
            String today = commonService.getDate();
            Element todayElement= element.getElementsByTag("Rect").last().getElementsByAttributeValue("data-date", today).first();
            StringTokenizer st= new StringTokenizer(todayElement.text(), " ");
            String commitString=st.nextToken();
            int commitCount=0;
            if(!commitString.equals("No")){
                commitCount=Integer.parseInt(commitString);
            }
            System.out.printf("총 %d개 커밋하셨습니다.\n", commitCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 신대득
     * baekjoonId를 받아서 그 사람이 푼 문제들을 클롤링하는 메서드
     * @param baekjoonId
     * @return
     */
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
     * 매일 오후 11시 50분에 메서드를 실행시킬 스케줄러
     *  **/
    @Scheduled(cron = "0 50 23 * * ?") // 매일 오후 11시 50분
    public void createDailyRecord(){
        log.info("createDailyRecord 시작");
        // 현재 진행중인 UserChallenge 중에 Category가 ALGO인 것들을 조회해서 저장시킨다.

        String today=commonService.getDate();

        // 현재 진행중인 알고리즘 챌린지 리스트 조회
        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByDateAndCategory(today, "ALGO");

        System.out.println("유저 챌린지 리스트는 : "+userChallengeList);
        // Algo 기록 저장
        for(UserChallenge userChallenge:userChallengeList){
            createAlgoRecord(ChallengeRecordRequestDto.from(userChallenge.getUserId(), userChallenge.getChallengeRoom().getId()));
        }
    }

    /**
     * 신대득
     * 알고리즘 문제 풀이 인증 기록을 저장하는 메서드
     * @param requestDto
     */
    @Override
    public void createAlgoRecord(ChallengeRecordRequestDto requestDto) {
        log.info("챌린지id " + requestDto.getChallengeRoomId() + "유저아이디id" + requestDto.getUserId());
        ChallengeRoomResponseDto challengeRoom = readChallenge(requestDto.getChallengeRoomId());
        UserResponseDto user = userServiceClient.getUserInfo(requestDto.getUserId()).getData();

        //오늘 날짜
        String date = commonService.getDate();

        // user의 solved ac에서 오늘 푼 문제들만 조회하기!
        List<DateProblemResponseDto> todayProblemList = userServiceClient.getDateBaekjoonList(user.getUserId(), date, date).getData();

        System.out.println("today problem list is :"+todayProblemList);
        // challengeRoom에서 최소 알고리즘 개수 가져오기 미달이면 Exception 발생
        if(todayProblemList.size()<challengeRoom.getAlgorithmCount()){
            throw new ApiException(ExceptionEnum.CONFIRM_FAILURE_ALGO_EXCEPTION);
        }

        // UserChallenge 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), requestDto.getUserId()).orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        log.info("[userChallenge id값]" + userChallenge.getId());

        // 기존에 이 날짜에 인증기록이 있는지 검사
        List<AlgoRecordResponseDto> checkRecordList = challengeRecordRepository.findByCreateAtAndUserChallenge(date, userChallenge);
        System.out.println("checkRecordList is : "+checkRecordList);
        if(checkRecordList.size()>0){
            log.error("이미 인증기록이 존재합니다.");
            throw new ApiException(ExceptionEnum.EXIST_CHALLENGE_RECORD);
        }

        ChallengeRecord challengeRecord = ChallengeRecord.fromAlgo(date, todayProblemList.size() , userChallenge);
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

    /** 사진 인증 상세 조회 (로그인이 반드시 되어있어야함) **/
    public PhotoRecordDetailResponseDto getPhotoRecordDetail(Long userId,Long challengeRecordId){

        ChallengeRecord challengeRecord = challengeRecordRepository.findById(challengeRecordId).orElseThrow(()->new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));

        //TODO 1. 인증 기록의 사용자 닉네임을 user-service로 사용자 정보를 요청한다.
        Long writeUserId =challengeRecord.getUserChallenge().getUserId();
        log.info("[인증 기록 작성자] : "+ writeUserId);

        UserResponseDto userResponseDto = userServiceClient.getUserInfo(writeUserId).getData();
        log.info("[인증 기록 작성자 닉네임] : " + userResponseDto.getNickname());

        //TODO 2. 인증 기록을 조회하는 사용자의 신고 기록을 리턴해야한다. 존재하면 true를 그렇지 않으면 false를 리턴한다.
        boolean reportStatus = reportRecordRepository.existsByUserIdAndChallengeRecordId(userId,challengeRecordId) ? true : false;

        PhotoRecordDetailResponseDto responseDto = new PhotoRecordDetailResponseDto(challengeRecord,userResponseDto.getNickname(),reportStatus);

        return responseDto;
    }


    /** 사진 인증 신고하기 (로그인, 권한 인증) **/
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
        int participants = challengeRoom.getCurParticipantsSize();
        int reportStandard = participants % 2 != 0 ? (participants / 2) + 1 : participants / 2;

        //TODO  3. 만약 신고기록 저장후에 해당 사진인증이 방 인원 전체의 절반 과 방장의 신고를 받은 기록이 있다면 해당 인증 기록은 실패로 처리한다.
        if(reportStandard >= challengeRecord.getReportCount() && challengeRecord.isHostReport() ) challengeRecord.setSuccessFail();

    }


    /**
     * author  :홍금비
     * explain : ChallengeRoom Entity를 조회 후 반환한다.
     * @param challengeRoomId 챌린지방 ID
     * @return ChallengeRoom Entity
     */
    @Override
    public ChallengeRoom getChallengeRoomEntity(Long challengeRoomId) {

        ChallengeRoom challengeRoom = challengeRoomRepository.findChallengeRoomById(challengeRoomId)
                .orElseThrow(()->new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));
        return challengeRoom;
    }
}
