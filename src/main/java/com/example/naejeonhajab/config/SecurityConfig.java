package com.example.naejeonhajab.config;

import com.example.naejeonhajab.security.JwtSecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationEntryPoint entryPoint;
    private final JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 활성화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // method 상관없이 요청 허용
                        .requestMatchers(
                                "/health"
                        ).permitAll()


                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight 요청 허용
                        .requestMatchers(POST,"/api/v1/users/**").permitAll() // 유저 API 허용
                        .requestMatchers(POST, "/api/v1/game/lol/*").permitAll() // 특정 POST 요청 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .addFilterBefore(jwtSecurityFilter, SecurityContextHolderAwareRequestFilter.class) // JWT 필터 추가
                .formLogin(AbstractHttpConfigurer::disable) // Form Login 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .logout(AbstractHttpConfigurer::disable) // Logout 비활성화
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint)) // EntryPoint 설정
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 허용된 Origin 추가
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",      // 로컬 개발용
                "https://naejeonhaja.com",    // 메인 도메인
                "https://www.naejeonhaja.com", // www 서브도메인
                "https://server.naejeonhaja.com" // 서버 서브도메인
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용된 HTTP 메서드
        config.setAllowedHeaders(List.of("Content-Type", "Authorization")); // 허용된 헤더
        config.setAllowCredentials(true); // 인증 정보 허용
        config.setMaxAge(3600L); // Preflight 요청 캐싱 시간
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
