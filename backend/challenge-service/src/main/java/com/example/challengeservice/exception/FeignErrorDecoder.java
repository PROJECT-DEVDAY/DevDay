package com.example.challengeservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
public class FeignErrorDecoder implements ErrorDecoder {
//    private final Environment env;

    @Override
    public Exception decode(String methodKey, Response response) {
        switch(response.status()){
            case 400:
                break;
            case 404:
                if(methodKey.contains("getUserBaekjoonList")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "user-service의 BaekjoonList를 불러올 수 없습니다.");
                    // env.getProperty("order_service.exception.order_is_empty")와 같은 방법도 사용 가능
                } else if(methodKey.contains("createProblem")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "user-service의 createProblem을 실행할 수 없습니다.");
                }else if(methodKey.contains("getUserInfo")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "user-service의 getUserInfo을 실행할 수 없습니다.");
                }else if(methodKey.contains("getDateBaekjoonList")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "user-service의 getDateBaekjoonList을 실행할 수 없습니다.");
                }
                break;

            default:
                return new Exception(response.reason());
        }
        return null;
    }
}