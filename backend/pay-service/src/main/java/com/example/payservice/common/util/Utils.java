package com.example.payservice.common.util;

import com.example.payservice.common.exception.ApiException;
import com.example.payservice.common.exception.ExceptionEnum;

import javax.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    /**
     * HttpServletRequest로부터 header에서 userId를 조회합니다. 없을 경우, 권한이 없음으로 간주합니다.
     * @param request
     * @return
     */
    public static long parseAuthorizedUserId(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getHeader("userId"));
        } catch (NumberFormatException ex) {
            throw new ApiException(ExceptionEnum.MEMBER_AUTHORIZED_EXCEPTION);
        }
    }
}
