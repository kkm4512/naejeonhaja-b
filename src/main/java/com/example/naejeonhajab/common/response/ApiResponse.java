package com.example.naejeonhajab.common.response;

import com.example.naejeonhajab.common.response.enums.ApiResponseEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {
    private final HttpStatus httpStatus;
    private final String message;
    private T data;

    // 데이터, 상태코드, 메시지 반환 생성자 (ApiResponseEnumImpl)
    public ApiResponse(ApiResponseEnum apiResponseEnum) {
        this.httpStatus = apiResponseEnum.getHttpStatus();
        this.message = apiResponseEnum.getMessage();
    }

    // 데이터, 상태코드, 메시지 반환 생성자 (ApiResponseEnumImpl)
    public ApiResponse(ApiResponseEnum apiResponseEnum, T data) {
        this.httpStatus = apiResponseEnum.getHttpStatus();
        this.message = apiResponseEnum.getMessage();
        this.data = data;
    }

    // 데이터, 상태코드, 메시지 반환 생성자 (data,httpStatus,message)
    public ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    // 상태코드, 메세지만 반환시 사용 (서비스 -> 컨트롤러)
    public static <T> ApiResponse<T> of(ApiResponseEnum apiResponseEnum) {
        return new ApiResponse<>(apiResponseEnum);
    }

    // 데이터, 상태코드, 메세지만 반환시 사용 (서비스 -> 컨트롤러)
    public static <T> ApiResponse<T> of(ApiResponseEnum apiResponseEnum, T data) {
        return new ApiResponse<>(apiResponseEnum,data);
    }
}
