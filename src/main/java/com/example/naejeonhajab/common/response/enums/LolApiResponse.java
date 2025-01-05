package com.example.naejeonhajab.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LolApiResponse implements ApiResponseEnum {

    // 302
    LOL_PLAYER_FOUND(HttpStatus.FOUND, "검증된 플레이어"),

    // 400
    LOL_TITLE_NOT_NULL(HttpStatus.BAD_REQUEST, "제목은 공란 일 수 없습니다"),

    // 401

    // 404
    LOL_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 롤 팀내역은 존재하지 않습니다"),
    LOL_RESULT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 롤 대전결과는 존재하지 않습니다"),
    LOL_PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 플레이어")
    ;

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
