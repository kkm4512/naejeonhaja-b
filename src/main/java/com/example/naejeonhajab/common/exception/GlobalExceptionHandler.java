package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> baseException(BaseException e) {
        ApiResponseEnum apiResponseEnum = e.getApiResponseEnum();
        ApiResponse<Void> apiResponse = new ApiResponse<>(apiResponseEnum);
        return ResponseEntity.status(apiResponse.getCode()).body(apiResponse);
    }


}
