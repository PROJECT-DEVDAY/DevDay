package com.example.payservice.dto.nhbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NH오픈플랫폼 간편결제(입금이체)를 위한 파라미터로 활용됩니다.
 * 추가적인 정보는 여기서 확인 가능합니다.
 * @see <a href="https://developers.nonghyup.com/guide/GU_1000">농협 오픈플랫폼 Header</a>
 *
 * @author djunnni
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    // 테스트 API 명
    @JsonProperty("ApiNm")
    private String apiNm;

    // API 호출일자(YYYYMMDD)
    @JsonProperty("Tsymd")
    private String tsymd;

    // API 전송시각(HHMMSS)
    @JsonProperty("Trtm")
    private String trtm;

    // 기관 약정 코드
    @JsonProperty("Iscd")
    private String iscd;

    // [약정] 핀테크 앱 일련번호 테스트 시 001로 고정
    @JsonProperty("FintechApsno")
    private String fintechApsno;

    // [약정] 핀테크 API 서비스 코드
    @JsonProperty("ApiSvcCd")
    private String apiSvcCd;

    // 핀테크 서비스별 & 전송일자별 거래고유번호 번호
    // 테스트 시에는 API 요청시 매번 새로운 값으로!
    @JsonProperty("IsTuno")
    private String isTuno;
    // Access Token
    @JsonProperty("AccessToken")
    private String accessToken;

    /**
     * 농협 API로 사용될 헤더를 반환합니다.
     * @param env
     * @param apiNm
     * @return
     */
    public static Header createNHApiHeader(Environment env, String apiNm) {
        Date now = new Date();
        String dateTime = new SimpleDateFormat("yyyyMMddHHmmssSSSS")
                .format(now);
        return Header.builder()
                .apiNm(apiNm)
                .tsymd(dateTime.substring(0, 8))
                .trtm(dateTime.substring(8, 14))
                .iscd(env.getProperty("openapi.nonghyup.iscd"))
                .fintechApsno(env.getProperty("openapi.nonghyup.fintechApsno"))
                .apiSvcCd(env.getProperty("openapi.nonghyup.apiSvcCd"))
                .isTuno(String.valueOf(now.getTime()))
                .accessToken(env.getProperty("openapi.nonghyup.accessToken"))
                .build();
    }
}
