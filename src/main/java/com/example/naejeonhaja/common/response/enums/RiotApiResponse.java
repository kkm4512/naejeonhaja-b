package com.example.naejeonhaja.common.response.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RiotApiResponse implements ApiResponseEnum {
    RIOT_PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "플레이어를 찾을 수 없습니다."),
    RIOT_API_ERROR(HttpStatus.BAD_GATEWAY, "Riot API 호출에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
