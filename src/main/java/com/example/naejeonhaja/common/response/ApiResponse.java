package com.example.naejeonhaja.common.response;

import com.example.naejeonhaja.common.response.enums.ApiResponseEnum;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int code;
    private final String message;
    private final T datas;

    private ApiResponse(ApiResponseEnum responseEnum, T datas) {
        this.code = responseEnum.getCode().value();
        this.message = responseEnum.getMessage();
        this.datas = datas;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.datas = null;
    }

    public static <T> ApiResponse<T> of(ApiResponseEnum responseEnum, T datas) {
        return new ApiResponse<>(responseEnum, datas);
    }

    public static ApiResponse<Void> of(ApiResponseEnum responseEnum) {
        return new ApiResponse<>(responseEnum, null);
    }
}
