package com.example.challengeservice.service;

import com.example.challengeservice.client.UserServiceClient;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.*;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<SimpleChallengeResponseDto> getListSimpleChallenge(String category,String search,int size,Long offset ) {
        //searchParam :검색에 필요한 조건을 담은 객체
        SearchParam searchParam = new SearchParam(category,search,size,offset,commonService.getDate());

        List<ChallengeRoom> challengeRooms = challengeRoomRepository.getSimpleChallengeList(searchParam);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SimpleChallengeResponseDto>>() {}.getType(); // 리스트 타입 지정
        List<SimpleChallengeResponseDto> dtoList = modelMapper.map(challengeRooms, listType); // 변환

        log.info(dtoList.size()+" 찾은 방 개수");
        log.info(commonService.getDate()+" 현재 날짜");

        return dtoList;
    }

    /**
     * author : 홍금비
     * explain : 챌린지방  생성
     * @param challengeRoomRequestDto ChallengeRoom를 생성하는데 필요한 필드 값
     * @return 생성된 ChallengeRoomID
     * @throws IOException
     * retouch : 생성된 ChallengeRoom을 save하기전에 S3에 이미지를 업로드 하는것이 아니라 save가 되면 그 때 이미지를 업로드 할 수 있도록 변경
     */

    @Transactional
    @Override
    public ChallengeCreateResponseDto createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto)  throws IOException {

        String successUrl = "";
        String failUrl = "";
        String backgroundUrl = "";
        String s3DirName ="ChallengeRoom";

        //[예외 체크] 1. 자유 챌린지인 경우 , 인증 성공 , 실패에 대한 이미지 파일값이 존재한지 확인한다.
        if(challengeRoomRequestDto.getCategory().equals("FREE")){
            if(challengeRoomRequestDto.getCertSuccessFile()==null || challengeRoomRequestDto.getCertFailFile()==null)
                throw new ApiException(ExceptionEnum.CHALLENGE_FILE_PARAMETER_EXCEPTION);
        }

        ChallengeRoom challengeRoom = ChallengeRoom.from(challengeRoomRequestDto);
        Long challengeId = challengeRoomRepository.save(challengeRoom).getId();

            if(challengeRoomRequestDto.getBackGroundFile()==null) {

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
                    default: break;
                }
            }else{
                    backgroundUrl = amazonS3Service.upload(challengeRoomRequestDto.getBackGroundFile(),s3DirName);
                    challengeRoom.setBackGroundUrl(backgroundUrl);
                }

        if(challengeRoomRequestDto.getCategory().equals("FREE")) {
            //인증 성공,실패의 사진을 업로드
            successUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertSuccessFile(),s3DirName);
            failUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertFailFile(),s3DirName);
            challengeRoom.setCertificationUrl(successUrl,failUrl);
        }

        //챌린지 방이 잘 생성 되었다면 방을 만든 방장은 방에 참가해야한다.
        //joinChallenge(new ChallengeJoinRequestDto(challengeRoomRequestDto.getHostId(),challengeId,challengeRoomRequestDto.getNickname()));

        return ChallengeCreateResponseDto.from(challengeId,"챌린지 방 생성 완료");
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
        ChallengeRoomResponseDto challengeRoomResponseDto=mapper.map(challengeRoom, ChallengeRoomResponseDto.class);
        // Todo: 방장이 없어진 경우 처리해야함
        UserResponseDto userResponseDto = userServiceClient.getUserInfo(challengeRoom.getHostId()).getData();
        challengeRoomResponseDto.setHostNickname(userResponseDto.getNickname());
        challengeRoomResponseDto.setHostCount(challengeRoomRepository.countByHostId(userResponseDto.getUserId()));
        challengeRoomResponseDto.setHostProfileImage(userResponseDto.getProfileImageUrl());
        return challengeRoomResponseDto;
    }

    public Map<Long, ChallengeInfoResponseDto> challengeInfoList(List<Long> challengeIdList){
        Map<Long, ChallengeInfoResponseDto> challengeInfoResponseDtoMap=new HashMap<>();
        for(Long challengeId: challengeIdList){
            ChallengeRoom challengeRoom = challengeRoomRepository.findChallengeRoomById(challengeId)
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
     * @param joinRequestDto 챌린지방 입장에 필요한 정보 (유저id , 챌린지방id ,유저닉네임)
     * @return
     * retouch : 홍금비 : find 대신 exist 함수로 존재 유무 확인  Optional<UserChallenge> -> UserChallenge orElseThrow로 변경
     *           해당 챌린지에 참여가능한 인원 수가 넘으면 참가할 수 없도록 예외처리 작성
     *          홍금비: 유저가 방에 참여할때 자신의 닉네임도 UserChallenge에 닉네임을 저장하도록 함
     *           */

    @Override
    @Transactional
    public  String  joinChallenge(ChallengeJoinRequestDto joinRequestDto) {

        //해당 challengeRoom Entity 조회 -> 조회하지 않는 챌린지면 참가 할 수 없음
        ChallengeRoom challengeRoom = getChallengeRoomEntity(joinRequestDto.getChallengeRoomId());


        //해당 userId가 챌린지방에 참여했는지 확인
        boolean isJoinUser  = userChallengeRepository.existsByChallengeRoomIdAndUserId(joinRequestDto.getChallengeRoomId(), joinRequestDto.getUserId());

        //참가한 기록이 없다면 userChallenge(참가자 목록) 에 저장하고 challengeRoom 의 현재 참가 인원을 +1 한다.
        if(!isJoinUser) {
            UserChallenge userChallenge = UserChallenge.from(challengeRoom, joinRequestDto.getUserId(),joinRequestDto.getNickname());
            userChallengeRepository.save(userChallenge);
            challengeRoom.plusCurParticipantsSize(); // +1 증가
        }else {
            throw new ApiException(ExceptionEnum.ALREADY_JOIN_CHALLENGEROOM);
        }

        // 예외 처리 :참여 인원이 전부 찬 경우는 해당 챌린지에 참여할 수 없음
        if (challengeRoom.getCurParticipantsSize() == challengeRoom.getMaxParticipantsSize() ) {
            throw  new ApiException(ExceptionEnum.UNABLE_TO_JOIN_CHALLENGEROOM);
        }

        return "참가 성공";
    }

    public  String  checkJoinChallenge(ChallengeJoinRequestDto joinRequestDto) {
        //해당 challengeRoom Entity 조회 -> 조회하지 않는 챌린지면 참가 할 수 없음
        ChallengeRoom challengeRoom = getChallengeRoomEntity(joinRequestDto.getChallengeRoomId());

        //해당 userId가 챌린지방에 참여했는지 확인
        boolean isJoinUser  = userChallengeRepository.existsByChallengeRoomIdAndUserId(joinRequestDto.getChallengeRoomId(), joinRequestDto.getUserId());
        if(isJoinUser){
            throw new ApiException(ExceptionEnum.ALREADY_JOIN_CHALLENGEROOM);
        }
        // 예외 처리 :참여 인원이 전부 찬 경우는 해당 챌린지에 참여할 수 없음
        if (challengeRoom.getCurParticipantsSize() == challengeRoom.getMaxParticipantsSize() ) {
            throw  new ApiException(ExceptionEnum.UNABLE_TO_JOIN_CHALLENGEROOM);
        }
        return "True";
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
        return UserChallengeInfoResponseDto.from(
                userChallengingList.size(),
                userChallengedList.size(),
                userHostChallengesList.size()
        );
    }

    /**
     * 신대득
     * githubId로 commit 정보들을 가져오는 메서드
     * @param githubId
     */
    @Override
    public CommitCountResponseDto getGithubCommit(String githubId){
        String githubUrl = "https://github.com/";
        githubUrl+=githubId;
        Connection conn = Jsoup.connect(githubUrl);
        List<String> solvedList= new ArrayList<>();
        int commitCount=0;
        try {
            Document document = conn.get();
            Elements imageUrlElements = document.getElementsByClass("js-calendar-graph-svg");
            Element element = imageUrlElements.get(0);
            String today = commonService.getDate();
            Element todayElement= element.getElementsByTag("Rect").last().getElementsByAttributeValue("data-date", today).first();
            StringTokenizer st= new StringTokenizer(todayElement.text(), " ");
            String commitString=st.nextToken();
            if(!commitString.equals("No")){
                commitCount=Integer.parseInt(commitString);
            }
            System.out.printf("총 %d개 커밋하셨습니다.\n", commitCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return CommitCountResponseDto.from(githubId, commitCount);
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
            log.info("총 %d 문제 풀이하셨습니다\n", count);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SolvedListResponseDto.from(solvedList, count);
    }

    public void updateChallengeRoom(Long challengeRoomId){
        log.info("updateChallengeRoom 실행");
        ChallengeRoom challengeRoom = getChallengeRoomEntity(challengeRoomId);
        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByChallengeRoomId(challengeRoom.getId());
        for(UserChallenge uc :userChallengeList){
            if(challengeRoom.getCategory().equals("ALGO")){
                log.info("updateUserBaekjoon 실행");
                updateUserBaekjoon(uc.getUserId());
                log.info("createAlgoRecord 실행");
                createAlgoRecord(ChallengeRecordRequestDto.from(uc.getUserId(),challengeRoomId));
            } else if(challengeRoom.getCategory().equals("COMMIT")){
                log.info("updateUserCommit 실행");
                updateUserCommit(uc.getUserId());
                log.info("createCommitRecord 실행");
                createCommitRecord(ChallengeRecordRequestDto.from(uc.getUserId(),challengeRoomId));
            }
        }
    }

    /** 유저의 백준리스트 업데이트
     * Todo : 예외처리 추가
     * **/
    public void updateUserBaekjoon(Long userId){
        SingleResult<UserResponseDto> userResponseDtoTemp = userServiceClient.getUserInfo(userId);
        log.info("userResponseDto is : {}",userResponseDtoTemp);
        UserResponseDto userResponseDto=userResponseDtoTemp.getData();
        SingleResult<BaekjoonListResponseDto> baekjoonListResponseDto = userServiceClient.getUserBaekjoonList(userResponseDto.getUserId());

        String baekjoonId = baekjoonListResponseDto.getData().getBaekjoonId();
        if(baekjoonId==null){
            return;
//            throw new ApiException(ExceptionEnum.ALGO_NOT_EXIST_ID);
        }
        List<String> diffSolvedList=new ArrayList<>();
        Map<String, String> problemList = baekjoonListResponseDto.getData().getProblemList();
        List<String> newSolvedList=solvedProblemList(baekjoonId).getSolvedList();
        for(String s:newSolvedList){
            if(problemList.get(s)==null){
                diffSolvedList.add(s);
            }
        }
        if(diffSolvedList.size()==0){
            return;
//            throw new ApiException(ExceptionEnum.ALGO_ALREADY_UPDATE);
        }
        userServiceClient.createProblem(userId, ProblemRequestDto.from(diffSolvedList));
    }

    /** 유저의 커밋리스트 업데이트
     * Todo : 예외처리 추가
     * **/
    public void updateUserCommit(Long userId) {
        SingleResult<UserResponseDto> userResponseDtoTemp = userServiceClient.getUserInfo(userId);
        UserResponseDto userResponseDto = userResponseDtoTemp.getData();

        String today = commonService.getDate();
        // 오늘 날짜의 유저의 커밋 수 조회
        CommitCountResponseDto commitCountResponseDto=getGithubCommit(userResponseDto.getGithub());
        log.info("commitCountResponseDto is : {}", commitCountResponseDto);

//        if(commitCountResponseDto.getCommitCount()==0){
//            return;
//        }
        userServiceClient.updateCommitCount(userResponseDto.getUserId(), new CommitRequestDto(today, commitCountResponseDto.getCommitCount()));
    }

    /**
     * 신대득
     * 선택한 유저가 선택한 날짜에
     * 커밋 개수를 반환하는 메서드
     */
    public CommitResponseDto checkDateUserCommit(Long userId, String selectDate){
        updateUserCommit(userId);
        UserResponseDto userInfo = userServiceClient.getUserInfo(userId).getData();
        CommitResponseDto commitResponseDto = userServiceClient.getCommitRecord(userId, selectDate).getData();
        return commitResponseDto;
    }

    /**
     * 신대득
     * 선택한 유저가 선택한 날짜에
     * 푼 문제 리스트를 반환하는 메서드
     */
    public SolvedListResponseDto checkDateUserBaekjoon(Long userId, String selectDate){
        UserResponseDto userInfo = userServiceClient.getUserInfo(userId).getData();
        List<DateProblemResponseDto> dateBaekjoonList = userServiceClient.getDateBaekjoonList(userInfo.getUserId(), selectDate, selectDate).getData();
        List<String> problemList=new ArrayList<>();
        for(DateProblemResponseDto problem:dateBaekjoonList){
            problemList.add(problem.getProblemId());
        }
        return SolvedListResponseDto.from(userInfo.getUserId(), problemList, problemList.size(), selectDate);
    }

    /**
     * 해당 유저의 최근 5일 (오늘 ~ 4일전)
     * 푼 문제 리스트를 반환하는 메서드
     */
    @Override
    public SolvedMapResponseDto getRecentUserBaekjoon(Long userId) {
        String today= commonService.getDate();
        String pastDay=commonService.getPastDay(5, commonService.getDate());

        List<DateProblemResponseDto> dateBaekjoonList =userServiceClient.getDateBaekjoonList(userId,pastDay,today).getData();
        Map<String, List<String>> myMap = new HashMap<>();
        for(int i=0;i<=5;i++){
            myMap.putIfAbsent(commonService.getPastDay(i,commonService.getDate()), new ArrayList<>());
        }
        for(DateProblemResponseDto dateProblemResponseDto: dateBaekjoonList){
            String curDate=dateProblemResponseDto.getSuccessDate();
            List<String> problemList= myMap.get(curDate);
            problemList.add(dateProblemResponseDto.getProblemId());
        }
        return SolvedMapResponseDto.algoFrom(myMap);
    }

    @Override
    public SolvedMapResponseDto getRecentUserCommit(Long userId) {
//        updateUserCommit(userId); // Todo : 커밋 최신화 속도가 느리다면 빼야함!!!!
//        createDailyRecord(); // 기록 최신 저장 매번 하느라 느려질 것 같음..
        String today=commonService.getDate();
        String pastDay=commonService.getPastDay(5, today);
        log.info("today is {} , pastDay is {}", today, pastDay);
        List<CommitResponseDto> dateCommitList = userServiceClient.getDateCommitList(userId, pastDay, today).getData();

        Map<String, Integer> myMap = new HashMap<>();

        for(CommitResponseDto commitResponseDto: dateCommitList){
            myMap.put(commitResponseDto.getCommitDate(), commitResponseDto.getCommitCount());
        }

        return SolvedMapResponseDto.commitFrom(myMap);
    }

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
        List<UserChallenge> userAlgoChallengeList = userChallengeRepository.findAllByDateAndCategory(today, "ALGO");

        // Algo 기록 저장
        for(UserChallenge userChallenge:userAlgoChallengeList){
            createAlgoRecord(ChallengeRecordRequestDto.from(userChallenge.getUserId(), userChallenge.getChallengeRoom().getId()));
        }
        
        // 현재 진행중인 커밋 챌린지 리스트 조회
        List<UserChallenge> userCommitChallengeList = userChallengeRepository.findAllByDateAndCategory(today, "COMMIT");
        // Commit 기록 저장
        for(UserChallenge userChallenge:userCommitChallengeList){
            createCommitRecord(ChallengeRecordRequestDto.from(userChallenge.getUserId(), userChallenge.getChallengeRoom().getId()));
        }

    }

    /**
     * 신대득
     * 하루정산 시키기 (1일전 인증기록을 통해서)
     *
     */
    @Scheduled(cron = "0 1 0 * * ?") // 매일 오후 0시 1분
    public void culcDailyPayment(){
        log.info("culcDailyPayment 시작");
        List<ChallengeRoom> challengingRoomList = challengeRoomRepository.findChallengingRoomByDate(commonService.getPastDay(0,commonService.getDate()));
        for(ChallengeRoom challengeRoom: challengingRoomList){
            oneDayCulc(challengeRoom);
        }
    }

    /**
     * 신대득
     * 알고리즘 문제 풀이 인증 기록을 저장하는 메서드
     * @param requestDto
     */
    @Override
    @Transactional
    public void createAlgoRecord(ChallengeRecordRequestDto requestDto) {
        log.info("챌린지id " + requestDto.getChallengeRoomId() + "유저아이디id" + requestDto.getUserId());
        // Todo : 방장이 없어진 경우 처리해야함
//        ChallengeRoomResponseDto challengeRoom = readChallenge(requestDto.getChallengeRoomId());
        ChallengeRoom challengeRoom = getChallengeRoomEntity(requestDto.getChallengeRoomId());

        UserResponseDto user = userServiceClient.getUserInfo(requestDto.getUserId()).getData();

        // 오늘 날짜
        String date = commonService.getDate();

        // user의 solved ac에서 오늘 푼 문제들만 조회하기!
        List<DateProblemResponseDto> todayProblemList = userServiceClient.getDateBaekjoonList(user.getUserId(), date, date).getData();

        // challengeRoom에서 최소 알고리즘 개수 가져오기 미달이면 Exception 발생
        if(todayProblemList.size()<challengeRoom.getAlgorithmCount()){
            return;
//            throw new ApiException(ExceptionEnum.CONFIRM_FAILURE_ALGO_EXCEPTION);
        }

        // UserChallenge 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), requestDto.getUserId()).orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        log.info("[userChallenge id값]" + userChallenge.getId());

        // 기존에 이 날짜에 인증기록이 있는지 검사
        Optional<ChallengeRecord> algoRecordResponseDto = challengeRecordRepository.findByCreateAtAndUserChallenge(date, userChallenge);
        if(algoRecordResponseDto.isPresent()){
            if(algoRecordResponseDto.get().getAlgorithmCount()!=todayProblemList.size()){
                algoRecordResponseDto.get().setAlgorithmCount(todayProblemList.size());
                challengeRecordRepository.save(algoRecordResponseDto.get());
            }
//            throw new ApiException(ExceptionEnum.EXIST_CHALLENGE_RECORD);
        }else{
            ChallengeRecord challengeRecord = ChallengeRecord.fromAlgo(date, todayProblemList.size(), userChallenge);
            challengeRecordRepository.save(challengeRecord);
        }
    }

    /**
     * 신대득
     * 커밋 인증 기록을 저장하는 메서드
     * @param requestDto
     */

    @Override
    @Transactional
    public void createCommitRecord(ChallengeRecordRequestDto requestDto) {
        ChallengeRoom challengeRoom = getChallengeRoomEntity(requestDto.getChallengeRoomId());
        UserResponseDto user = userServiceClient.getUserInfo(requestDto.getUserId()).getData();

        // 오늘 날짜
        String date = commonService.getDate();

        // user의 해당 날짜의 커밋 수 조회하기!
        SingleResult<CommitResponseDto> commitResponseDto = userServiceClient.getCommitRecord(user.getUserId(), date);
        // challengeRoom에서 최소 커밋 개수 가져오기 미달이면 Exception 발생
        if(commitResponseDto.getData().getCommitCount()<challengeRoom.getCommitCount()){
            return;
//            throw new ApiException(ExceptionEnum.CONFIRM_FAILURE_ALGO_EXCEPTION);
        }

        // UserChallenge 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), requestDto.getUserId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        log.info("[userChallenge id값]" + userChallenge.getId());

        // 기존에 이 날짜에 인증기록이 있는지 검사
        Optional<ChallengeRecord> commitRecordResponseDto = challengeRecordRepository.findByCreateAtAndUserChallenge(date, userChallenge);
        if(commitRecordResponseDto.isPresent()){
            // 개수가 다르다면 업데이트
            if(commitRecordResponseDto.get().getCommitCount()!=commitResponseDto.getData().getCommitCount()){
                commitRecordResponseDto.get().setCommitCount(commitResponseDto.getData().getCommitCount());
                challengeRecordRepository.save(commitRecordResponseDto.get());
            }
//            throw new ApiException(ExceptionEnum.EXIST_CHALLENGE_RECORD);
        }else{
            ChallengeRecord challengeRecord = ChallengeRecord.fromCommit(date, commitResponseDto.getData().getCommitCount(), userChallenge);
            challengeRecordRepository.save(challengeRecord);
        }
    }

    /** 하루 정산
     * 일단 현재 진행중인 챌린지 방을 받아와야함 입력값으로!!
     * **/
    @Override
    @Transactional
    public void oneDayCulc(ChallengeRoom challengeRoom) {
        Integer period = commonService.diffDay(challengeRoom.getStartDate(), challengeRoom.getEndDate()).intValue();
        int oneDayFee = (int)Math.ceil((double) challengeRoom.getEntryFee() / (period+1)); // 하루 가격

        String beforeOneDay = commonService.getPastDay(1, commonService.getDate()); // 어제 날짜 조회
        // 만약 계산하는 날짜가 챌린지 기간이 아니라면
        if(commonService.diffDay(beforeOneDay, challengeRoom.getEndDate())<0 || commonService.diffDay(challengeRoom.getStartDate(), beforeOneDay) <0){
            return;
        }

        // 방에 참여한 참여자들
        List<UserChallenge> userChallengeList = userChallengeRepository.findUserChallengesByChallengeRoomId(challengeRoom.getId());
        List<UserChallenge> successList=new ArrayList<>();
        List<UserChallenge> failList=new ArrayList<>();
        for (UserChallenge userChallenge : userChallengeList) {
            /**
             * 인증은 무조건 1개!
             * 여러개 들어올 경우 에러처리 필요
             */
            Optional<ChallengeRecordResponseDto> challengeRecord = challengeRecordRepository.findByUserChallengeIdAndCreateAt(userChallenge.getId(), beforeOneDay);
            if(!challengeRecord.isPresent()){ // 기록이 없다면 => 무조건 실패
                failList.add(userChallenge);
            }else{ // 기록이 있다면 (사진 인증인 경우 검사)
                if (!(challengeRecord.get().isSuccess())) { // 인증이 인정되지 않았다면
                    failList.add(userChallenge);
                } else{
                    successList.add(userChallenge);
                }
            }
        }
        // 실패자들 돈 뺐기
        Long sum=0L;
        for(UserChallenge userChallenge: failList){
            log.info("실패한 사람은 {}", userChallenge.getId());
            log.info("onedayFee is : {}",oneDayFee);
            userChallenge.setDiffPrice(userChallenge.getDiffPrice()-oneDayFee);
            userChallengeRepository.save(userChallenge);
            sum+=oneDayFee;
        }

        // 성공자들 돈 나눠 갖기
        Long todayMoney=Long.valueOf((long)Math.ceil((double)sum/successList.size()));
        for(UserChallenge userChallenge: successList){
            log.info("성공한 사람은 {}", userChallenge.getId());
            userChallenge.setDiffPrice(userChallenge.getDiffPrice()+todayMoney);
            userChallengeRepository.save(userChallenge);
        }

    }


    /**
     * 신대득
     * category에 따라 record를 만드는 메서드
     * 제작중!!
     * @param challengeRoomId :
     * @param userId
     * @param viewType
     * @param category
     * @return
     */
/*    public List<?> getSelfRecord(Long challengeRoomId, Long userId, String viewType, String category) {
        List<?> selfRecord=new ArrayList<>();
        switch(category){
            case "ALGO":
                break;
            case "COMMIT":
                break;
            case "FREE":
                //userChallenge 값을 찾아야함
                selfRecord = getSelfPhotoRecord(challengeRoomId,userId,viewType);
                break;
            default:
                break;
        }
        return selfRecord;
    }*/


    /**
     * author  :홍금비
     * explain : ChallengeRoom Entity를 조회 후 반환한다.
     * @param challengeRoomId 챌린지방 ID
     * @return ChallengeRoom Entity
     */
    @Override
    public ChallengeRoom getChallengeRoomEntity(Long challengeRoomId) {

        return challengeRoomRepository.findChallengeRoomById(challengeRoomId)
                .orElseThrow(()->new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));
    }


    /**
     * author : 홍금비
     * explain : 내가 참여중인 챌린지 목록 가져오기
     * @param userId : 유저 아이디
     * @return MyChallengeResponseDto
     * retouch :  무한 스크롤 방식으로 변경
     */

    @Override
    public List<MyChallengeResponseDto> getMyChallengeList(Long userId , String status , Long offset , String search , int size) {

        return challengeRoomRepository.findMyChallengeList(userId,status,commonService.getDate() , offset ,search ,size );
    }

    /**
     * 신대득
     * 알고리즘
     * 나의 인증현황
     * 진행률, 예치금 + 상금, 성공 / 실패 횟수
     */
    @Override
    public ProgressResponseDto getProgressUserBaekjoon(Long userId, Long challengeId){
        log.info("getProgressUserBaekjoon 실행");
        ChallengeRoomResponseDto challengeRoom=readChallenge(challengeId);

        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoom.getId(), userId)
                .orElseThrow(()-> new ApiException(ExceptionEnum.USER_CHALLENGE_LIST_NOT_EXIST));
        // 진행률 계산
        Long challengeLength = commonService.diffDay(challengeRoom.getStartDate(), commonService.getDate());
        if(challengeLength<0){
            challengeLength=-1L;
        }
        challengeLength++;
        Long successCount=0L;
        List<ChallengeRecord> challengeRecordList = challengeRecordRepository.findAllByUserChallengeIdAndStartDateAndEndDate(userChallenge.getId(), challengeRoom.getStartDate(), challengeRoom.getEndDate(), true);
        for(ChallengeRecord cr:challengeRecordList){
            switch(challengeRoom.getCategory()){
                case "ALGO":
                    if(cr.getAlgorithmCount()>=challengeRoom.getAlgorithmCount())
                        successCount++;
                    break;
                case "COMMIT":
                    if(cr.getCommitCount()>=challengeRoom.getCommitCount())
                        successCount++;
                    break;
                case "FREE":
                    if(cr.isSuccess())
                        successCount++;
                    break;
            }
        }
//        Long successCount=(long)challengeRecordList.size();
        Long failCount = challengeLength - successCount;
        if(challengeLength==0){
            challengeLength++;
        }
        String progressRate= String.format("%.2f", (double)(successCount*100)/challengeLength);
        // 예치금 + 상금
        Long curPrice=userChallenge.getDiffPrice();
        return new ProgressResponseDto(progressRate, curPrice, successCount, failCount);
    }


    /**
     * 신대득
     * 현재 챌린지 방의
     * 탑 랭크 리스트 조회
     * @param challengeId
     * @return
     */
    @Override
    public List<RankResponseDto> getTopRank(Long challengeId) {
        ChallengeRoom challengeRoom = getChallengeRoomEntity(challengeId);
        List<UserChallenge> userChallengesByChallengeRoomId = userChallengeRepository.findAllByChallengeRoomId(challengeRoom.getId());
        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();
        Long period = commonService.diffDay(challengeRoom.getStartDate(), challengeRoom.getEndDate()) + 1;
        for (UserChallenge uc : userChallengesByChallengeRoomId) {
            // 현재 uc 중 챌린지 기간 동안의 레코드 조회
            switch (challengeRoom.getCategory()) {
                case "ALGO":
                    List<ChallengeRecord> challengeAlgoRecordList = challengeRecordRepository.findAllByUserChallengeIdAndStartDateAndEndDateAlgo(uc.getId(), challengeRoom.getStartDate(), challengeRoom.getEndDate(), true, challengeRoom.getAlgorithmCount());
                    rankResponseDtoList.add(new RankResponseDto(0L, uc.getUserId(), uc.getNickname(), (long) challengeAlgoRecordList.size(), period - (long) challengeAlgoRecordList.size()));
                    break;
                case "COMMIT":
                    List<ChallengeRecord> challengeCommitRecordList = challengeRecordRepository.findAllByUserChallengeIdAndStartDateAndEndDateCommit(uc.getId(), challengeRoom.getStartDate(), challengeRoom.getEndDate(), true, challengeRoom.getCommitCount());
                    rankResponseDtoList.add(new RankResponseDto(0L, uc.getUserId(), uc.getNickname(), (long) challengeCommitRecordList.size(), period - (long) challengeCommitRecordList.size()));
                    break;
            }
            Collections.sort(rankResponseDtoList);
            for (int i = 0; i < rankResponseDtoList.size(); i++) {
                rankResponseDtoList.get(i).setRank((long) i + 1);
            }
        }
        return rankResponseDtoList;
    }
}
