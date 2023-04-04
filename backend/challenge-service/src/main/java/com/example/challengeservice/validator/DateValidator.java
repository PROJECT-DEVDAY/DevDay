package com.example.challengeservice.validator;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DateValidator {
    public static boolean validateDateFormat(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); //날짜 형식 엄격히 체크

        try {
            Date date = dateFormat.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
