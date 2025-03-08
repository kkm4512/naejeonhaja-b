package com.example.naejeonhajab.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserApiResponse implements ApiResponseEnum {

    // 400
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다"),
    NOT_MATCH_CODE(HttpStatus.BAD_REQUEST, "인증코드가 상이합니다"),
    NOT_MATCH_EMAIL(HttpStatus.BAD_REQUEST, "이메일은 kakao,naver,gmail만 가능합니다"),

    // 401
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT입니다"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다"),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT입니다"),
    JWT_NOT_SIGNATURED(HttpStatus.UNAUTHORIZED, "서명되지 않은 JWT입니다"),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED,"로그인후 이용해 주세요"),

    // 403
    NOT_ME(HttpStatus.FORBIDDEN,"권한이 존재 하지 않습니다"),

    // 404
    JWT_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),

    // 409
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이메일이 중복 되었습니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다, 관리자에게 문의 해주세요");
    ;

    private final HttpStatus httpStatus;
    private final String message;

    UserApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
