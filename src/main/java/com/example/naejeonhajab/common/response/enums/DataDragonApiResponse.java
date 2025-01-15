package com.example.naejeonhajab.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DataDragonApiResponse implements ApiResponseEnum {


    // 400
    DATA_DRAGON_API_BAD_REQUEST(HttpStatus.BAD_REQUEST, "DATA_DRAGON API 요청에 실패 하였습니다"),

    // 401

    // 404
    DATA_DRAGON_API_NOT_FOUND(HttpStatus.BAD_REQUEST, "DATA_DRAGON API 요청한 데이터는 조회되지 않습니다"),

    // 409

    // 500
    ;

    private final HttpStatus httpStatus;
    private final String message;

    DataDragonApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
