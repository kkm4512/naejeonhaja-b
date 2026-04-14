package com.example.naejeonhaja.common.response.enums;

import org.springframework.http.HttpStatus;

public interface ApiResponseEnum {
    HttpStatus getStatus();
    String getMessage();
}
