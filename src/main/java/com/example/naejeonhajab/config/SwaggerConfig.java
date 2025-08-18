package com.example.naejeonhajab.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "API 문서", version = "v1"),
        security = @SecurityRequirement(name = "BearerAuth") // 전역 적용
)
@SecurityScheme(
        name = "BearerAuth", // 위의 이름과 일치시켜야 함
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT" // 선택사항
)
public class SwaggerConfig {
}
