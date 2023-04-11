package com.example.challengeservice.service;

import com.example.challengeservice.client.UserServiceClient;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.*;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.repository.*;
import com.example.challengeservice.service.challenge.BasicChallengeService;
import com.example.challengeservice.service.common.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeServiceImpl implements ChallengeService{
    private final UserServiceClient userServiceClient;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRoomRepository challengeRoomRepository;
    private final CommonServiceImpl commonService;
    private final ChallengeRecordRepository challengeRecordRepository;

    private final BasicChallengeService basicChallengeService;


    /**
     * githubId로 commit 정보가져오기
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return CommitCountResponseDto.from(githubId, commitCount);
    }

    /**
     * explain : 회원이 푼 백준 문제 가져오기
     * @param baekjoonId :백준 아이디
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

    /** 챌린지 수정하기 **/
    public void updateChallengeRoom(Long challengeRoomId){

        ChallengeRoom challengeRoom = basicChallengeService.getChallengeRoomEntity(challengeRoomId);
        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByChallengeRoomId(challengeRoom.getId());
        for(UserChallenge uc :userChallengeList){
            if(challengeRoom.getCategory().equals("ALGO")){

                updateUserBaekjoon(uc.getUserId());

                createAlgoRecord(ChallengeRecordRequestDto.from(uc.getUserId(),challengeRoomId));
            } else if(challengeRoom.getCategory().equals("COMMIT")){

                updateUserCommit(uc.getUserId());

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
        ChallengeRoom challengeRoom = basicChallengeService.getChallengeRoomEntity(requestDto.getChallengeRoomId());

        UserResponseDto user = userServiceClient.getUserInfo(requestDto.getUserId()).getData();

        // 오늘 날짜
        String date = commonService.getDate();


        List<DateProblemResponseDto> todayProblemList = userServiceClient.getDateBaekjoonList(user.getUserId(), date, date).getData();

        //challengeRoom에서 최소 알고리즘 개수 가져오기 미달이면 Exception 발생
        if(todayProblemList.size()<challengeRoom.getAlgorithmCount()){
            return;
        }

        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), requestDto.getUserId()).orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));


        // 기존에 이 날짜에 인증기록이 있는지 검사
        Optional<ChallengeRecord> algoRecordResponseDto = challengeRecordRepository.findByCreateAtAndUserChallenge(date, userChallenge);
        if(algoRecordResponseDto.isPresent()){
            if(algoRecordResponseDto.get().getAlgorithmCount()!=todayProblemList.size()){
                algoRecordResponseDto.get().setAlgorithmCount(todayProblemList.size());
                challengeRecordRepository.save(algoRecordResponseDto.get());
            }

        }else{
            ChallengeRecord challengeRecord = ChallengeRecord.fromAlgo(date, todayProblemList.size(), userChallenge);
            challengeRecordRepository.save(challengeRecord);
        }
    }

    /**
     * 커밋 인증 기록을 저장하는 메서드
     */
    @Override
    @Transactional
    public void createCommitRecord(ChallengeRecordRequestDto requestDto) {
        ChallengeRoom challengeRoom = basicChallengeService.getChallengeRoomEntity(requestDto.getChallengeRoomId());
        UserResponseDto user = userServiceClient.getUserInfo(requestDto.getUserId()).getData();

        String date = commonService.getDate();


        SingleResult<CommitResponseDto> commitResponseDto = userServiceClient.getCommitRecord(user.getUserId(), date);

        if(commitResponseDto.getData().getCommitCount()<challengeRoom.getCommitCount()){
            return;
        }


        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(requestDto.getChallengeRoomId(), requestDto.getUserId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        log.info("[userChallenge id값]" + userChallenge.getId());


        Optional<ChallengeRecord> commitRecordResponseDto = challengeRecordRepository.findByCreateAtAndUserChallenge(date, userChallenge);
        if(commitRecordResponseDto.isPresent()){

            if(commitRecordResponseDto.get().getCommitCount()!=commitResponseDto.getData().getCommitCount()){
                commitRecordResponseDto.get().setCommitCount(commitResponseDto.getData().getCommitCount());
                challengeRecordRepository.save(commitRecordResponseDto.get());
            }

        }else{
            ChallengeRecord challengeRecord = ChallengeRecord.fromCommit(date, commitResponseDto.getData().getCommitCount(), userChallenge);
            challengeRecordRepository.save(challengeRecord);
        }
    }

    /**
     * 하루 정산
     * 일단 현재 진행중인 챌린지 방을 받아와야함 입력값으로!!
     * **/
    @Override
    @Transactional
    public void oneDayCulc(ChallengeRoom challengeRoom) {
        Integer period = commonService.diffDay(challengeRoom.getStartDate(), challengeRoom.getEndDate()).intValue();
        int oneDayFee = (int)Math.ceil((double) challengeRoom.getEntryFee() / (period+1));
        String beforeOneDay = commonService.getPastDay(0, commonService.getDate());

        if(commonService.diffDay(beforeOneDay, challengeRoom.getEndDate())<0 || commonService.diffDay(challengeRoom.getStartDate(), beforeOneDay) <0){
            return;
        }

        List<UserChallenge> userChallengeList = userChallengeRepository.findUserChallengesByChallengeRoomId(challengeRoom.getId());
        List<UserChallenge> successList=new ArrayList<>();
        List<UserChallenge> failList=new ArrayList<>();
        for (UserChallenge userChallenge : userChallengeList) {

            List<ChallengeRecordResponseDto> challengeRecord = challengeRecordRepository.findByUserChallengeIdAndCreateAt(userChallenge.getId(), beforeOneDay);
            if(challengeRecord.size()==0){
                failList.add(userChallenge);
            }else{
                if (!(challengeRecord.get(0).isSuccess())) {
                    failList.add(userChallenge);
                } else{
                    successList.add(userChallenge);
                }
            }
        }

        Long sum=0L;
        for(UserChallenge userChallenge: failList){

            userChallenge.setDiffPrice(userChallenge.getDiffPrice()-oneDayFee);
            userChallengeRepository.save(userChallenge);
            sum+=oneDayFee;
        }

        Long todayMoney=Long.valueOf((long)Math.ceil((double)sum/successList.size()));
        for(UserChallenge userChallenge: successList){
            userChallenge.setDiffPrice(userChallenge.getDiffPrice()+todayMoney);
            userChallengeRepository.save(userChallenge);
        }

    }

    /**
     * 알고리즘
     * 나의 인증현황
     * 진행률, 예치금 + 상금, 성공 / 실패 횟수
     */
    @Override
    public ProgressResponseDto getProgressUserBaekjoon(Long userId, Long challengeId){

        ChallengeRoomResponseDto challengeRoom=basicChallengeService.readChallenge(challengeId);

        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoom.getId(), userId)
                .orElseThrow(()-> new ApiException(ExceptionEnum.USER_CHALLENGE_LIST_NOT_EXIST));

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
        Long failCount = challengeLength - successCount;
        if(challengeLength==0){
            challengeLength++;
        }
        String progressRate= String.format("%.2f", (double)(successCount*100)/challengeLength);

        Long curPrice=userChallenge.getDiffPrice() + (long)challengeRoom.getEntryFee();
        return new ProgressResponseDto(progressRate, curPrice, successCount, failCount);
    }


    /**
     * 현재 챌린지 방의 탑 랭크 리스트 조회
     */
    @Override
    public List<RankResponseDto> getTopRank(Long challengeId) {
        ChallengeRoom challengeRoom = basicChallengeService.getChallengeRoomEntity(challengeId);
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
                case "FREE":
                    List<ChallengeRecord> challengeFreeRecordList = challengeRecordRepository.findAllByUserChallengeIdAndStartDateAndEndDateFree(uc.getId(), challengeRoom.getStartDate(), challengeRoom.getEndDate(), true);
                    rankResponseDtoList.add(new RankResponseDto(0L, uc.getUserId(), uc.getNickname(), (long) challengeFreeRecordList.size(), period-(long)challengeFreeRecordList.size()));
                    break;
            }
            Collections.sort(rankResponseDtoList);
            for (int i = 0; i < rankResponseDtoList.size(); i++) {
                rankResponseDtoList.get(i).setRank((long) i + 1);
            }
        }
        return rankResponseDtoList;
    }

    /**
      현재 유저가 들어온 방에서 자신의 진행율을 계산하는 메서드
     */
    public String getProgressRate(Long userChallengeId, ChallengeRoomResponseDto challengeRoom){

        Long challengeLength = commonService.diffDay(challengeRoom.getStartDate(), challengeRoom.getEndDate());
        if(challengeLength < 0){
            challengeLength = -1L;
        }
        challengeLength++;
        Long successCount = 0L;
        List<ChallengeRecord> challengeRecordList = challengeRecordRepository.findAllByUserChallengeIdAndStartDateAndEndDate(userChallengeId, challengeRoom.getStartDate(), challengeRoom.getEndDate(), true);
        if(challengeRecordList.size() == 0){
            return "0";
        }
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
        if(challengeLength==0){
            challengeLength++;
        }
        return String.format("%.2f", (double)(successCount*100)/challengeLength);
    }

    /**
     * 유저가 선택한 챌린지의 인증서 정보를 조회하는 메서드
     */
    public CertificationResponseDto getCertification(Long userId, Long challengeRoomId){
        ChallengeRoomResponseDto challengeRoom = basicChallengeService.readChallenge(challengeRoomId);
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoomId, userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        UserResponseDto userResponseDto= userServiceClient.getUserInfo(userId).getData();
        String progressRate= getProgressRate(userChallenge.getId(), challengeRoom);

        return new CertificationResponseDto(userResponseDto.getName(), userChallenge.getChallengeRoom().getTitle(), userChallenge.getChallengeRoom().getStartDate(),
                userChallenge.getChallengeRoom().getEndDate(), progressRate, userChallenge.getId());
    }
}
