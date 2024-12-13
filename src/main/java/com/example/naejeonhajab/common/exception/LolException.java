package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;

public class LolException extends BaseException {
    public LolException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public LolException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
