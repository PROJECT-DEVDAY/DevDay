package com.example.challengeservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    USER_CHALLENGE_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST,"UC0001","이미 이 챌린지에 존재하는 유저입니다."),
    CHALLENGE_BAD_REQUEST(HttpStatus.BAD_REQUEST ,"UC002" ,"요청한 값이 유효하지 않습니다."),
    CHALLENGE_RECORD_BAD_REQUEST(HttpStatus.BAD_REQUEST ,"UC003" ,"날짜가 다릅니다.."),
    CHALLENGE_RECORD_SELF_REPORT(HttpStatus.BAD_REQUEST ,"UC003" ,"자기 자신의 인증 기록은 신고하실 수 없습니다."),
    CHALLENGE_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "C0003", "존재하지 않는 챌린지 입니다."),
    USER_CHALLENGE_LIST_NOT_EXIST(HttpStatus.NOT_FOUND, "UC0003", "이 유저의 챌린지가 존재하지 않습니다."),
    USER_CHALLENGE_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "C0004", "해당 챌린지에 존재하지 참여자 입니다."),
    CONFIRM_FAILURE_ALGO_EXCEPTION(HttpStatus.NOT_FOUND, "CF0001", "인증에 실패했습니다. 알고리즘 인증 회수가 부족합니다."),

    NOT_EXIST_REPORT_RECORD(HttpStatus.CONFLICT ,"F001","신고 기록이 존재합니다."),
    NOT_EXIST_CHALLENGE_RECORD(HttpStatus.NOT_FOUND,"C222","존재하지 않은 인증 기록입니다"),


    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "내부 문제로 다음번에 다시 시도해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0002", "내부 문제로 다음번에 다시 시도해주세요."),
    API_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "E0003", "존재하지 않는 API 입니다."),
    API_METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "E0004", "지원하지 않는 Method 입니다."),
    API_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST, "E0005", "파라미터 타입과 값을 확인하세요."),
    MEMBER_ACCESS_EXCEPTION(HttpStatus.FORBIDDEN, "M0001", "접근 권한이 없습니다."),
    MEMBER_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND,"M0003", "존재하지 않는 계정입니다."),
    PASSWORD_NOT_MATCHED_EXCEPTION(HttpStatus.FORBIDDEN, "M0004", "비밀번호가 일치하지 않습니다."),
    CODE_NOT_MATCHED_EXCEPTION(HttpStatus.FORBIDDEN, "M0005", "인증 코드가 일치하지 않습니다."),
    MEMBER_INFO_NOT_MATCHED_EXCEPTION(HttpStatus.NOT_FOUND, "M0006", "일치하는 유저가 없습니다."),
    EMAIL_AUTH_NOT_FOUNT_EXCEPTION(HttpStatus.NOT_FOUND, "E0001", "이메일 인증 요청이 존재하지 않습니다."),
    EMAIL_ACCEPT_TIMEOUT_EXCEPTION(HttpStatus.NOT_FOUND, "E0002", "이메일 인증 요청 시간이 만료되었습니다."),
    EMAIL_NOT_ACCEPT_EXCEPTION(HttpStatus.FORBIDDEN, "E0003", "이메일이 인증되지 않았습니다.");




    private final HttpStatus status;
    private final String code;
    private final String message;

}