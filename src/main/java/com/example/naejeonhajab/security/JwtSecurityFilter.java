package com.example.naejeonhajab.security;

import com.example.naejeonhajab.common.exception.UserException;
import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import static com.example.naejeonhajab.common.response.enums.UserApiResponse.*;
import static com.example.naejeonhajab.security.JwtManager.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtManager jm;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, @NonNull HttpServletResponse httpResponse, @NonNull FilterChain chain) throws ServletException, IOException {
        String jwt = httpRequest.getHeader(AUTHORIZATION_HEADER);
        if (jwt != null) {
            try {
                Claims claims = jm.toClaims(jwt);
                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                String nickname = claims.get("nickname", String.class);
                String userRoleString = claims.get("userRole", String.class);
                UserRole userRole = UserRole.valueOf(userRoleString);

                // 사용자 인증이 아직 설정되지 않았다면
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // AuthUser 객체를 생성
                    AuthUser authUser = new AuthUser(userId, email, nickname, userRole);

                    // JwtAuthenticationToken으로 인증 객체 생성
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                    // SecurityContextHolder에 인증 객체 설정 (사용자 인증 처리)
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        // 요청을 다음 필터로 넘김
        chain.doFilter(httpRequest, httpResponse);
    }
}

