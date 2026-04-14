package com.example.naejeonhaja.common.exception;

import com.example.naejeonhaja.common.response.enums.ApiResponseEnum;

public class RiotException extends BaseException {
    public RiotException(ApiResponseEnum responseEnum) {
        super(responseEnum);
    }
}
