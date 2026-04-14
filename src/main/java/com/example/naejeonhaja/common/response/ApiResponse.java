package com.example.naejeonhaja.common.response;

import com.example.naejeonhaja.common.response.enums.ApiResponseEnum;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int code;
    private final String message;
    private final T data;

    private ApiResponse(ApiResponseEnum responseEnum, T data) {
        this.code = responseEnum.getCode().value();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public static <T> ApiResponse<T> of(ApiResponseEnum responseEnum, T data) {
        return new ApiResponse<>(responseEnum, data);
    }

    public static ApiResponse<Void> of(ApiResponseEnum responseEnum) {
        return new ApiResponse<>(responseEnum, null);
    }
}
