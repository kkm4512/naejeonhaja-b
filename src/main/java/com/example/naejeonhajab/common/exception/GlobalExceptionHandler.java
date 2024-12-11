package com.example.naejeonhajab.common.exception;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // @Valid 유효성 검사 실패 시 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(BaseApiResponse.FAIL.getMessage());
        ApiResponse<Void> apiResponse = new ApiResponse<>(BaseApiResponse.FAIL.getHttpStatus(), errorMessage, null);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Validated 유효성 예외처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse(BaseApiResponse.FAIL.getMessage());
        ApiResponse<Void> apiResponse = new ApiResponse<>(BaseApiResponse.FAIL.getHttpStatus(), errorMessage, null);
        return ResponseEntity.badRequest().body(apiResponse);
    }


}
