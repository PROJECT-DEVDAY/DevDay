package com.example.challengeservice.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {

    /** 오늘의 (년,월,일) **/
    @Override
    public String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /** n일 전의 날짜 얻기 **/
    @Override
    public String getPastDay(int n ,String dateString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = null;
        try {
            Date date = formatter.parse(dateString);

            cal = new GregorianCalendar(Locale.KOREA);
            cal.setTime(date);
            cal.add(Calendar.DATE,-n);

        } catch (ParseException e) {
            e.printStackTrace(); // 예외 정보를 출력하는 코드
        }

        return formatter.format(cal.getTime());
    }

    /** startDate~ endDate의 기간 얻기 **/
    @Override
    public Long diffDay(String startDate, String endDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Long diffDays = 0L;

        try {
            Date stDate = format.parse(startDate);
            Date enDate = format.parse(endDate);

            long diff = enDate.getTime() - stDate.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);

        } catch(Exception e){
            e.printStackTrace();
        }
        return diffDays;
    }
}
