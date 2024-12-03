package com.example.naejeonhajab.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseApiResponse implements ApiResponseEnum {

    //200
    SUCCESS(HttpStatus.OK, "요청하신 작업에 성공 하였습니다"),

    TEAM_MISMATCH(HttpStatus.BAD_REQUEST, "팀을 만들 수 없습니다, 라인을 더 폭넓혀 보세요 ! (아니면 티어가 너무 안맞을수도 ㅎ;)"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    BaseApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
