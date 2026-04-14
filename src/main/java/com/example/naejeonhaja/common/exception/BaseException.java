package com.example.naejeonhaja.common.exception;

import com.example.naejeonhaja.common.response.enums.ApiResponseEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ApiResponseEnum responseEnum;

    public BaseException(ApiResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
    }
}
