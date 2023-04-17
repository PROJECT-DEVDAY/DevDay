package com.example.challengeservice.service.commit;

import com.example.challengeservice.client.UserServiceClient;
import com.example.challengeservice.client.dto.CommitResponseDto;
import com.example.challengeservice.client.dto.UserResponseDto;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.CommitRequestDto;
import com.example.challengeservice.dto.response.CommitCountResponseDto;
import com.example.challengeservice.dto.response.SolvedMapResponseDto;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.repository.ChallengeRecordRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import com.example.challengeservice.service.challenge.BasicChallengeService;
import com.example.challengeservice.service.common.CommonService;
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
public class CommitChallengeServiceImpl implements CommitChallengeService{
    private final CommonService commonService;
    private final UserServiceClient userServiceClient;
    private final BasicChallengeService basicChallengeService;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;

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

    /** 유저의 커밋리스트 업데이트 **/
    public void updateUserCommit(Long userId) {
        SingleResult<UserResponseDto> userResponseDtoTemp = userServiceClient.getUserInfo(userId);
        UserResponseDto userResponseDto = userResponseDtoTemp.getData();

        String today = commonService.getDate();
        // 오늘 날짜의 유저의 커밋 수 조회
        CommitCountResponseDto commitCountResponseDto=getGithubCommit(userResponseDto.getGithub());
        if(commitCountResponseDto.getCommitCount()==0){
            return;
        }
        userServiceClient.updateCommitCount(userResponseDto.getUserId(), new CommitRequestDto(today, commitCountResponseDto.getCommitCount()));
    }

    /**
     * 신대득
     * 선택한 유저가 선택한 날짜에
     * 커밋 개수를 반환하는 메서드
     */
    public CommitResponseDto checkDateUserCommit(Long userId, String selectDate){
        updateUserCommit(userId);
        CommitResponseDto commitResponseDto = userServiceClient.getCommitRecord(userId, selectDate).getData();
        return commitResponseDto;
    }

    /** 해당 유저가 최근 5일 동안의 커밋 개수 조회 **/
    @Override
    public SolvedMapResponseDto getRecentUserCommit(Long userId) {
        String today=commonService.getDate();
        String pastDay=commonService.getPastDay(5, today);
        List<CommitResponseDto> dateCommitList = userServiceClient.getDateCommitList(userId, pastDay, today).getData();

        Map<String, Integer> myMap = new HashMap<>();

        for(CommitResponseDto commitResponseDto: dateCommitList){
            myMap.put(commitResponseDto.getCommitDate(), commitResponseDto.getCommitCount());
        }
        return SolvedMapResponseDto.commitFrom(myMap);
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
}
