package com.example.naejeonhaja.common.response.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LolApiResponse implements ApiResponseEnum {
    LOL_PLAYER_FOUND(HttpStatus.OK, "플레이어를 찾았습니다."),
    LOL_PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "플레이어를 찾을 수 없습니다."),
    LOL_INVALID_PLAYER_COUNT(HttpStatus.BAD_REQUEST, "플레이어 수가 올바르지 않습니다.");

    private final HttpStatus code;
    private final String message;
}
