package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j(topic = "BaseException")
public class BaseException extends RuntimeException {
    private final ApiResponseEnum apiResponseEnum;
    private final String originalMessage; // 원본 메시지 저장

    public BaseException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum.getMessage());
        this.apiResponseEnum = apiResponseEnum;
        this.originalMessage = null; // 원본 메시지가 없을 경우
    }

    // 원초적 e.getMessage()를 가져오기 위함
    public BaseException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum.getMessage(), cause);
        this.apiResponseEnum = apiResponseEnum;
        this.originalMessage = cause.getMessage(); // 원초적 메시지 저장
        log.error(cause.getMessage(), cause);
    }

}
