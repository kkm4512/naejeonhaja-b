package com.example.naejeonhaja.common.exception;

import com.example.naejeonhaja.common.response.enums.ApiResponseEnum;

public class LolException extends BaseException {
    public LolException(ApiResponseEnum responseEnum) {
        super(responseEnum);
    }
}
