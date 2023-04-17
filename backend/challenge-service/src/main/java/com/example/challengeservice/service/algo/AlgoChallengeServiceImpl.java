package com.example.challengeservice.service.algo;

import com.example.challengeservice.client.UserServiceClient;
import com.example.challengeservice.client.dto.BaekjoonListResponseDto;
import com.example.challengeservice.client.dto.DateProblemResponseDto;
import com.example.challengeservice.client.dto.UserResponseDto;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ProblemRequestDto;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.dto.response.ProgressResponseDto;
import com.example.challengeservice.dto.response.SolvedListResponseDto;
import com.example.challengeservice.dto.response.SolvedMapResponseDto;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.repository.ChallengeRecordRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import com.example.challengeservice.service.challenge.BasicChallengeService;
import com.example.challengeservice.service.common.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AlgoChallengeServiceImpl implements AlgoChallengeService{
    private final UserServiceClient userServiceClient;
    private final UserChallengeRepository userChallengeRepository;
    private final CommonServiceImpl commonService;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final BasicChallengeService basicChallengeService;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SolvedListResponseDto.from(solvedList, count);
    }

    /** 유저의 백준리스트 업데이트
     * **/
    public void updateUserBaekjoon(Long userId){
        SingleResult<UserResponseDto> userResponseDtoTemp = userServiceClient.getUserInfo(userId);
        UserResponseDto userResponseDto=userResponseDtoTemp.getData();
        SingleResult<BaekjoonListResponseDto> baekjoonListResponseDto = userServiceClient.getUserBaekjoonList(userResponseDto.getUserId());

        String baekjoonId = baekjoonListResponseDto.getData().getBaekjoonId();
        if(baekjoonId==null){
            return;
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
        }
        userServiceClient.createProblem(userId, ProblemRequestDto.from(diffSolvedList));
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

    /**
     * 신대득
     * 알고리즘 문제 풀이 인증 기록을 저장하는 메서드
     * @param requestDto
     */
    @Override
    @Transactional
    public void createAlgoRecord(ChallengeRecordRequestDto requestDto) {
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
}
