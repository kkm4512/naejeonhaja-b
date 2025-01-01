package com.example.naejeonhajab.domain.dto.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
public class MailProperties {

    private MailConfig naver;
    private MailConfig gmail;
    private MailConfig kakao;

    @Getter
    @Setter
    public static class MailConfig {
        private String email;
        private String password;
    }
}

