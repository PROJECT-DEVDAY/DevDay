package com.example.payservice.dto.bank;


public enum Bank {
    KYONGNAMBANK("039", "경남은행"),
    GWANGJUBANK("034", "광주은행"),
    LOCALNONGHYEOP("012", "단위농협(지역농축협)"),
    BUSANBANK("032", "부산은행"),
    SAEMAUL("045", "새마을금고"),
    SANLIM("064","산림조합"),
    SHINHAN("088","신한은행"),
    SHINHYEOP("048","신협"),
    CITI("027", "씨티은행"),
    WOORI("020", "우리은행"),
    POST("071", "우체국예금보험"),
    JEONBUKBANK("037", "전북은행"),
    JEJUBANK("035", "제주은행"),
    KAKAOBANK("090", "카카오뱅크"),
    KBANK("089", "케이뱅크"),
    TOSSBANK("092", "토스뱅크"),
    HANA("081", "하나은행"),
    HSBC("054", "홍콩상하이은행"),
    DAEGUBANK("031", "대구은행"),
    IBK("003", "IBK기업은행"),
    KOOKMIN("004", "국민은행"),
    KDBBANK("002", "산업은행"),
    NONGHYEOP("011", "NH농협은행"),
    SC("023", "SC제일은행"),
    SUHYEOP("007", "SH수협은행");

    private final String code;
    private final String name;

    Bank(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
