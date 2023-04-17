package com.example.challengeservice.service;

import com.example.challengeservice.client.PayServiceClient;
import com.example.challengeservice.client.dto.ChallengeSettlementRequest;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.repository.ChallengeRoomRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import com.example.challengeservice.service.algo.AlgoChallengeService;
import com.example.challengeservice.service.challenge.BasicChallengeService;
import com.example.challengeservice.service.commit.CommitChallengeService;
import com.example.challengeservice.service.common.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService{

    private final ChallengeRoomRepository challengeRoomRepository;
    private final PayServiceClient payServiceClient;
    private final CommonService commonService;
    private final UserChallengeRepository userChallengeRepository;
    private final BasicChallengeService basicChallengeService;
    private final CommitChallengeService commitChallengeService;
    private final AlgoChallengeService algoChallengeService;

    @Override
    public void endChallengeCalculate() {
      payServiceClient.requestChallengeSettlement(new ChallengeSettlementRequest(challengeRoomRepository.findClosedChallengeUser(commonService.getPastDay(0,commonService.getDate()))));
    }

    /** 신대득
     * 인증 정보 저장 (알고리즘)
     * 매일 오후 11시 50분에 메서드를 실행시킬 스케줄러
     *  **/
    @Scheduled(cron = "0 50 23 * * ?") // 매일 오후 11시 50분
    public void createDailyRecord(){
        String today=commonService.getDate();

        // 현재 진행중인 알고리즘 챌린지 리스트 조회
        List<UserChallenge> userAlgoChallengeList = userChallengeRepository.findAllByDateAndCategory(today, "ALGO");

        // Algo 기록 저장
        for(UserChallenge userChallenge:userAlgoChallengeList){
            algoChallengeService.createAlgoRecord(ChallengeRecordRequestDto.from(userChallenge.getUserId(), userChallenge.getChallengeRoom().getId()));
        }

        // 현재 진행중인 커밋 챌린지 리스트 조회
        List<UserChallenge> userCommitChallengeList = userChallengeRepository.findAllByDateAndCategory(today, "COMMIT");
        // Commit 기록 저장
        for(UserChallenge userChallenge:userCommitChallengeList){
            commitChallengeService.createCommitRecord(ChallengeRecordRequestDto.from(userChallenge.getUserId(), userChallenge.getChallengeRoom().getId()));
        }
    }

    /**
     * 신대득
     * 하루정산 시키기 (1일전 인증기록을 통해서)
     */
    @Scheduled(cron = "0 1 0 * * ?") // 매일 오후 0시 1분
    public void culcDailyPayment(){
        List<ChallengeRoom> challengingRoomList = challengeRoomRepository.findChallengingRoomByDate(commonService.getPastDay(0,commonService.getDate()));
        for(ChallengeRoom challengeRoom: challengingRoomList){
            basicChallengeService.oneDayCulc(challengeRoom);
        }
    }
}
