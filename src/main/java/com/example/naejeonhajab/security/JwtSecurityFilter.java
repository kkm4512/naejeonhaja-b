package com.example.naejeonhajab.security;

import com.example.naejeonhajab.common.exception.UserException;
import com.example.naejeonhajab.config.SecurityWhitelistConfig;
import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.naejeonhajab.common.response.enums.UserApiResponse.NOT_LOGIN;
import static com.example.naejeonhajab.security.JwtManager.AUTHORIZATION_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtManager jm;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        // 특정 Set<String>에 포함된 경로면 필터 제외 (빠른 조회)
        if (SecurityWhitelistConfig.PERMIT_METHODS.containsKey(method)) {
            for (String urlPattern : SecurityWhitelistConfig.PERMIT_METHODS.get(method)) {
                if (pathMatcher.match(urlPattern, requestURI)) {
                    return true; // 필터 비활성화
                }
            }
        }

        return false;
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
