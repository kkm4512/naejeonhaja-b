package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;

public class RiotException extends BaseException {
    public RiotException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public RiotException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
