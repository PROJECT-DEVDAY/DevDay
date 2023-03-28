package com.example.payservice.controller;

import com.example.payservice.common.exception.ApiException;
import com.example.payservice.common.exception.ApiExceptionEntity;
import com.example.payservice.common.exception.ExceptionEnum;
import com.example.payservice.exception.LackOfPrizeException;
import com.example.payservice.exception.PaymentsConfirmException;
import com.example.payservice.exception.PrizeWithdrawException;
import com.example.payservice.exception.UnRefundableException;
import com.example.payservice.exception.UserNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * API에서 발생한 에러를 핸들링합니다.
 */
@RestControllerAdvice
public class ExceptionAdviceController {

    /**
     * API Exception이 발생했을 경우, 예외를 client로 보냅니다.
     * @param e
     * @return
     */
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiExceptionEntity> apiExceptionHandler(final ApiException e) {

        return new ResponseEntity<>(
                new ApiExceptionEntity(
                        e.getError().getCode(),
                        e.getError().getMessage()
                ),
                e.getError().getStatus()
        );
    }
    @ExceptionHandler({PaymentsConfirmException.class})
    public ResponseEntity<ApiExceptionEntity> paymentsConfirmException() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.DEPOSIT_CHARGE_CONFIRM_EXCEPTION));
    }
    /**
     * 상금 환급에서 문제가 발생했을 경우 기본 처리입니다.
     * @return
     */
    @ExceptionHandler({PrizeWithdrawException.class})
    public ResponseEntity<ApiExceptionEntity> prizeWithdrawException() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.PRIZE_WITHDRAW_EXCEPTION));
    }
    /**
     * 출금금액이 상금보다 클 경우 기본 처리입니다.
     * @return
     */
    @ExceptionHandler({LackOfPrizeException.class})
    public ResponseEntity<ApiExceptionEntity> lackOfPrizeExceptionHandler() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.PRIZE_LACK_OF_MONEY_EXCEPTION));
    }

    /**
     * RuntimeException, Exception 발생시 내부서버에러(500)를 표시합니다.
     * @return
     */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ApiExceptionEntity> runTimeExceptionHandler() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.INTERNAL_SERVER_ERROR));
    }

    /**
     * 지원하지 않는 API요청이 왔을 경우 기본 처리입니다.
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiExceptionEntity> methodNotSupportExceptionHandler() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.API_METHOD_NOT_ALLOWED_EXCEPTION));
    }

    /**
     * 파라미터 검증에서 실패했을 경우 기본 처리입니다.
     * @return
     */
    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiExceptionEntity> parameterException() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION));
    }

    /**
     * Spring Security 에 권한이 없는 사용자가 접근 시 기본 처리입니다.
     * @return
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiExceptionEntity> accessDeniedException() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.MEMBER_ACCESS_EXCEPTION));
    }

    /**
     * 존재하지 않는 유저일 경우 기본 처리입니다.
     * @return
     */
    @ExceptionHandler({UserNotExistException.class})
    public ResponseEntity<ApiExceptionEntity> userNotExistException() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
    }

    /**
     * 환불이 불가능한 유저에 대한 기본 처리입니다.
     */
    @ExceptionHandler({UnRefundableException.class})
    public ResponseEntity<ApiExceptionEntity> unRefundableException() {
        return apiExceptionHandler(new ApiException(ExceptionEnum.MEMBER_NOT_REFUNDABLE_EXCEPTION));
    }
}
