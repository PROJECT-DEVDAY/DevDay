package com.example.payservice.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    PRIZE_WITHDRAW_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"P0001","상금 환급에 문제가 발생했습니다."),
    PRIZE_LACK_OF_MONEY_EXCEPTION(HttpStatus.BAD_REQUEST, "P0002", "출금금액이 상금보다 큽니다."),
    DEPOSIT_LACK_OF_MONEY_EXCEPTION(HttpStatus.BAD_REQUEST, "P003", "출금금액이 예치금보다 큽니다."),
    DEPOSIT_CHARGE_CONFIRM_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "P0004", "예치금 충전 확인과정에서 문제가 발생했습니다."),

    MEMBER_NOT_REFUNDABLE_EXCEPTION(HttpStatus.BAD_REQUEST, "P0005", "환불 대상이 아닙니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0001", "내부 문제로 다음번에 다시 시도해주세요."),
    API_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND, "E0002", "존재하지 않는 API 입니다."),
    API_METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "E0003", "지원하지 않는 Method 입니다."),
    API_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST, "E0004", "파라미터 타입과 값을 확인하세요."),
    MEMBER_NOT_EXIST_EXCEPTION(HttpStatus.NOT_FOUND,"M0001", "존재하지 않는 계정입니다."),
    MEMBER_ACCESS_EXCEPTION(HttpStatus.FORBIDDEN, "M0002", "접근 권한이 없습니다."),
    MEMBER_AUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "M003", "인증되지 않은 계정의 요청입니다."),
    MEMBER_CAN_NOT_FIND_EXCEPTION(HttpStatus.BAD_REQUEST, "M004", "user-service로 부터 조회되지 않는 유저입니다.");




    private final HttpStatus status;
    private final String code;
    private final String message;

}
