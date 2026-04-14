package com.example.naejeonhaja.common.response.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseApiResponse implements ApiResponseEnum {
    SUCCESS(HttpStatus.OK, "요청이 성공했습니다."),
    TEAM_MISMATCH(HttpStatus.BAD_REQUEST, "팀 구성에 실패했습니다. 다시 시도해주세요.");

    private final HttpStatus status;
    private final String message;
}
