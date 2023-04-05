package com.example.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "내부 문제로 다음번에 다시 시도해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0002", "내부 문제로 다음번에 다시 시도해주세요."),
    API_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "E0003", "존재하지 않는 API 입니다."),
    API_METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "E0004", "지원하지 않는 Method 입니다."),
    API_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST, "E0005", "파라미터 타입과 값을 확인하세요."),
    MEMBER_ACCESS_EXCEPTION(HttpStatus.FORBIDDEN, "M0001", "접근 권한이 없습니다."),
    MEMBER_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST,"M0002","이미 존재하는 유저입니다."),
    MEMBER_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND,"M0003", "존재하지 않는 계정입니다."),
    PASSWORD_NOT_MATCHED_EXCEPTION(HttpStatus.FORBIDDEN, "M0004", "비밀번호가 일치하지 않습니다."),
    CODE_NOT_MATCHED_EXCEPTION(HttpStatus.FORBIDDEN, "M0005", "인증 코드가 일치하지 않습니다."),
    MEMBER_INFO_NOT_MATCHED_EXCEPTION(HttpStatus.NOT_FOUND, "M0006", "일치하는 유저가 없습니다."),
    PASSWORD_MATCHED_EXCEPTION(HttpStatus.FORBIDDEN, "M0007", "현재 사용중인 비밀번호로는 수정할 수 없습니다."),
    MEMBER_CANNOT_LEAVE_EXCEPTION(HttpStatus.FORBIDDEN, "M0008", "현재 참여중인 챌린지가 있어 탈퇴가 불가능합니다."),
    EMAIL_AUTH_NOT_FOUNT_EXCEPTION(HttpStatus.NOT_FOUND, "E0001", "이메일 인증 요청이 존재하지 않습니다."),
    EMAIL_ACCEPT_TIMEOUT_EXCEPTION(HttpStatus.NOT_FOUND, "E0002", "이메일 인증 요청 시간이 만료되었습니다."),
    EMAIL_NOT_ACCEPT_EXCEPTION(HttpStatus.FORBIDDEN, "E0003", "이메일이 인증되지 않았습니다."),
    SERVER_NOT_CONNECT_EXCEPTION(HttpStatus.SERVICE_UNAVAILABLE, "S0001", "서비스가 연결되지 않았습니다."),
    CHALLENGEINFO_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "S0002", "챌린지 정보가 존재하지 않습니다."),
    MONEYINFO_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "S0003", "상여금 및 예치금 정보가 존재하지 않습니다."),
    PROBLEMINFO_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "S0003", "문제 리스트 정보가 존재하지 않습니다."),
    COMMITRECORD_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "S0004", "커밋 기록이 존재하지 않습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

}