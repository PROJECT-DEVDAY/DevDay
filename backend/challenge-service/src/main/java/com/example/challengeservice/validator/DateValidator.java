package com.example.challengeservice.validator;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateValidator {
    public static boolean validateDateFormat(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
