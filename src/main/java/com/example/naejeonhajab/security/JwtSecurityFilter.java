package com.example.naejeonhajab.security;

import com.example.naejeonhajab.common.exception.UserException;
import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import io.jsonwebtoken.Claims;
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

import static com.example.naejeonhajab.common.response.enums.UserApiResponse.NOT_LOGIN;
import static com.example.naejeonhajab.security.JwtManager.AUTHORIZATION_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtManager jm;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();

        // 인증이 필요 없는 경로 설정 (SecurityConfig의 permitAll과 일치시켜야 함)
        return requestURI.startsWith("/api/v1/users") ||
                requestURI.equals("/api/v1/game/lol/rift") ||
                requestURI.equals("/api/v1/game/lol/abyss") ||
                requestURI.equals("/api/v1/game/lol/tft");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, @NonNull HttpServletResponse httpResponse, @NonNull FilterChain chain) throws ServletException, IOException {
        String jwt = httpRequest.getHeader(AUTHORIZATION_HEADER);

        try {
            if (jwt != null) {
                Claims claims = jm.toClaims(jwt);
                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                String nickname = claims.get("nickname", String.class);
                String userRoleString = claims.get("userRole", String.class);
                UserRole userRole = UserRole.valueOf(userRoleString);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    AuthUser authUser = new AuthUser(userId, email, nickname, userRole);
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            else {
                throw new UserException(NOT_LOGIN);
            }
            chain.doFilter(httpRequest, httpResponse);
        } catch (Exception e) {
            httpRequest.setAttribute("exception", e);
            throw e; // EntryPoint로 전달
        }
    }
}
