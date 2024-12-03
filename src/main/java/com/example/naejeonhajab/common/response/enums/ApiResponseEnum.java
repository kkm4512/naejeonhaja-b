package com.example.naejeonhajab.common.response.enums;

import org.springframework.http.HttpStatus;

public interface ApiResponseEnum {
    HttpStatus getHttpStatus();
    String getMessage();
}
