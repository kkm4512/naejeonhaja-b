package com.example.naejeonhaja.common.exception;

import com.example.naejeonhaja.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        log.error("BaseException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getResponseEnum().getStatus())
                .body(ApiResponse.of(e.getResponseEnum()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("ValidationException: {}", message);
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("UnhandledException: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(new ApiResponse<>(500, "서버 오류가 발생했습니다."));
    }
}
