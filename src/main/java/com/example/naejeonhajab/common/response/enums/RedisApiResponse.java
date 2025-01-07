package com.example.naejeonhajab.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RedisApiResponse implements ApiResponseEnum {

    // 200
    REDIS_INIT_SAVE(HttpStatus.OK, "이미 Redis에 저장된 데이터 입니다"),
    ;

    // 409

    // 500
    ;

    private final HttpStatus httpStatus;
    private final String message;

    RedisApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
