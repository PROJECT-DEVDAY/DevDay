package com.example.challengeservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    USER_CHALLENGE_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST,"UC0001","이미 이 챌린지에 존재하는 유저입니다."),
    CHALLENGE_BAD_REQUEST(HttpStatus.BAD_REQUEST ,"C002" ,"요청한 값이 유효하지 않습니다."),
    CHALLENGE_FILE_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST ,"C003" ,"인증 기준 사진의 값이 존재하지 않습니다."),
    CHALLENGE_RECORD_BAD_REQUEST(HttpStatus.BAD_REQUEST ,"UC003" ,"날짜가 다릅니다.."),
    CHALLENGE_RECORD_SELF_REPORT(HttpStatus.BAD_REQUEST ,"UC003" ,"자기 자신의 인증 기록은 신고하실 수 없습니다."),
    CHALLENGE_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "C0003", "존재하지 않는 챌린지 입니다."),
    USER_CHALLENGE_LIST_NOT_EXIST(HttpStatus.NOT_FOUND, "UC0003", "이 유저의 챌린지가 존재하지 않습니다."),

    ALREADY_JOIN_CHALLENGEROOM(HttpStatus.CONFLICT,"J001","이미 해당챌린지에 참여한 유저입니다."),
    UNABLE_TO_JOIN_CHALLENGEROOM(HttpStatus.FORBIDDEN,"J002","해당 챌린지에 참가 인원 수가 초과하였습니다."),
    ALGO_ALREADY_UPDATE(HttpStatus.BAD_REQUEST,"A0010","이미 해당 계정의 알고리즘 정보가 최신입니다."),
    ALGO_NOT_EXIST_ID(HttpStatus.NOT_FOUND, "A0020", "해당 유저의 백준 아이디를 찾을 수 없습니다."),

    USER_CHALLENGE_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "C0004", "해당 챌린지에 존재하지 않는 참여자 입니다."),
    CONFIRM_FAILURE_ALGO_EXCEPTION(HttpStatus.NOT_FOUND, "CF0001", "인증에 실패했습니다. 알고리즘 인증 회수가 부족합니다."),

    NOT_EXIST_REPORT_RECORD(HttpStatus.CONFLICT ,"F001","신고 기록이 존재합니다."),
    NOT_EXIST_CHALLENGE_RECORD(HttpStatus.NOT_FOUND,"C222","존재하지 않은 인증 기록입니다"),
    EXIST_CHALLENGE_RECORD(HttpStatus.BAD_REQUEST,"CR111","이미 해당 날짜에 인증 기록이 존재합니다."),


    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "내부 문제로 다음번에 다시 시도해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0002", "내부 문제로 다음번에 다시 시도해주세요."),
    API_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "E0003", "존재하지 않는 API 입니다."),
    API_METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "E0004", "지원하지 않는 Method 입니다."),
    API_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST, "E0005", "파라미터 타입과 값을 확인하세요.");





    private final HttpStatus status;
    private final String code;
    private final String message;

}