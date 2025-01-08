package com.example.naejeonhajab.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RiotApiResponse implements ApiResponseEnum {


    // 400
    RIOT_API_BAD_REQUEST(HttpStatus.BAD_REQUEST, "라이엇 API 요청에 실패 하였습니다"),

    // 401

    // 404
    ;

    // 409

    // 500
    ;

    private final HttpStatus httpStatus;
    private final String message;

    RiotApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
