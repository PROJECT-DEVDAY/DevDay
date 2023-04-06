package com.example.challengeservice.service;

public interface CommonService {

    /** 오늘의 (년,월,일) **/
    String getDate();

    /** n일 전의 날짜 얻기 **/
    String getPastDay(int n , String date);

    /** startDate~ endDate의 기간 얻기 **/
    Long diffDay(String startDate, String endDate);
}
