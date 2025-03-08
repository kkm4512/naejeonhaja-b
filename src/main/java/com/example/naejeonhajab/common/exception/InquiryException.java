package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;

public class InquiryException extends BaseException {
    public InquiryException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public InquiryException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
