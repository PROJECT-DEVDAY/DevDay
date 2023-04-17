package com.example.challengeservice.service;

public interface SchedulerService {

    /** 종료 챌린지 정산 하기 **/
    void endChallengeCalculate();
    /** 하루 기록을 저장시키는 스케줄러 **/
    void createDailyRecord();
    /** 하루 정산을 시키는 스케줄러 **/
    void culcDailyPayment();
}
