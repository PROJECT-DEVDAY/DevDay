package com.example.userservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 503) {
            return new ApiException(ExceptionEnum.SERVER_NOT_CONNECT_EXCEPTION);
        } else if (response.status() == 404) {
            if (methodKey.contains("getChallengeInfo")) {
                return new ApiException(ExceptionEnum.CHALLENGEINFO_NOT_EXIST_EXCEPTION);
            } else if (methodKey.contains("getMoneyInfo") || methodKey.contains("createUser")) {
                return new ApiException(ExceptionEnum.MONEYINFO_NOT_EXIST_EXCEPTION);
            }
        }
        return new Exception(response.reason());
    }
}
