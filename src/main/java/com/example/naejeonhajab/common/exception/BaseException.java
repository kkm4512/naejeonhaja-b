package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ApiResponseEnum apiResponseEnum;

    public BaseException(ApiResponseEnum apiResponseEnum) {
        this.apiResponseEnum = apiResponseEnum;
    }
}
