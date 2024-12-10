package com.example.naejeonhajab.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        // 응답 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 응답 헤더 설정
        response.setContentType("application/json;charset=UTF-8");
        // 커스텀 메시지 반환
        response.getWriter().write("{\"code\":403,\"message\":\"로그인후 이용해주세요!\",\"data\":null}");
    }
}