package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;

public class DataDragonException extends BaseException {
    public DataDragonException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public DataDragonException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
