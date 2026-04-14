package com.example.naejeonhaja.common.response;

import com.example.naejeonhaja.common.response.enums.ApiResponseEnum;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(ApiResponseEnum responseEnum, T data) {
        this.status = responseEnum.getStatus().value();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
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
