package com.example.challengeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Override
    public String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(date);
    }

    @Override
    public String getPastDay(int n ,String dateString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = null;
        try {
            Date date = formatter.parse(dateString);

             cal = new GregorianCalendar(Locale.KOREA);
            cal.setTime(date);
            cal.add(Calendar.DATE,-n);
            // 예외가 발생하지 않았을 경우의 처리 코드
        } catch (ParseException e) {
            // ParseException이 발생한 경우의 처리 코드
            e.printStackTrace(); // 예외 정보를 출력하는 코드
        }
        log.info("cal is : "+cal.getTime());

        //return formatter.format(date);
        return formatter.format(cal.getTime());
    }

    @Override
    public Long diffDay(String startDate, String endDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Long diffDays=0L;
        // 문자열 -> Date
        try {
            Date stDate = format.parse(startDate);
            Date enDate = format.parse(endDate);

            long diff = enDate.getTime() - stDate.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.println("날짜의 차이는 : " + diffDays);
        } catch(Exception e){
            e.printStackTrace();
        }
        return diffDays;
    }
}
