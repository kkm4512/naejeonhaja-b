package com.example.naejeonhajab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class SecurityWhitelistConfig {

    // 특정 HTTP 메서드별 허용 경로
    public static final Map<HttpMethod, List<String>> PERMIT_METHODS = Map.of(
            HttpMethod.POST, List.of(
                    "/api/v1/users/**",
                    "/api/v1/game/lol/*"
            ),
            HttpMethod.PUT, List.of(
                    "/api/v1/users/**"
            ),
            HttpMethod.GET, List.of(
                    "/api/v1/game/lol/riot/**",
                    "/api/v1/game/lol/dataDragon/**",
                    "/api/v1/inquiry/**",
                    "/health"
            ),
            HttpMethod.OPTIONS, List.of("/**") // Preflight 요청 허용
    );

}
