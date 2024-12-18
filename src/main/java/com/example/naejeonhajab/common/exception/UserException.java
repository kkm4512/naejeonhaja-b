package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;

public class UserException extends BaseException {
    public UserException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public UserException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
