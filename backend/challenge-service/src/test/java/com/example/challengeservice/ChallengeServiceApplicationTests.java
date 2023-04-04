package com.example.challengeservice;

import com.example.challengeservice.service.CommonService;
import com.example.challengeservice.service.CommonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ChallengeServiceApplicationTests {



    @Test
    void contextLoads() {


    }

    @Mock
    private SimpleDateFormat formatter;


    @InjectMocks
    private CommonServiceImpl commonService;

    @Test
    public void testGetPastDay() throws Exception {
        // given
        Date date = new Date();
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(date);

       // when(formatter.format(any())).thenReturn("2023-04-04"); // formatter.format() 메서드가 호출될 때 반환할 값을 정의합니다.

        // when
        String result = commonService.getPastDay(5 ,"2023-03-31");

        // then
      //  verify(formatter, times(1)).format(any()); // formatter.format() 메서드가 1회 호출되었는지 확인합니다.
        assertEquals("2023-03-31", result); // 예상되는 반환값과 실제 반환값을 비교하여 테스트합니다.
    }
}
