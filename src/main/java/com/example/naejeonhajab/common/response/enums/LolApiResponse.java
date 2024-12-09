package com.example.naejeonhajab.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LolApiResponse implements ApiResponseEnum {


    // 401

    // 404
    LOL_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 롤 팀내역은 존재하지 않습니다")

    // 409

    // 500
    ;

    private final HttpStatus httpStatus;
    private final String message;

    LolApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
