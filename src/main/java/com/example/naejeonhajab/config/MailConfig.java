package com.example.naejeonhajab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean(name = "naverMailSender")
    public JavaMailSender naverMailSender() {
        return createMailSender(
                "smtp.naver.com",
                465,
                mailProperties.getNaver().getEmail(),
                mailProperties.getNaver().getPassword(),
                true
        );
    }

    @Bean(name = "gmailMailSender")
    public JavaMailSender gmailMailSender() {
        return createMailSender(
                "smtp.gmail.com",
                587,
                mailProperties.getGmail().getEmail(),
                mailProperties.getGmail().getPassword(),
                false
        );
    }

    @Bean(name = "kakaoMailSender")
    public JavaMailSender kakaoMailSender() {
        return createMailSender(
                "smtp.kakao.com",
                465,
                mailProperties.getKakao().getEmail(),
                mailProperties.getKakao().getPassword(),
                true
        );
    }

    private JavaMailSender createMailSender(String host, int port, String username, String password, boolean useSsl) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        if (useSsl) {
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            props.put("mail.smtp.starttls.enable", "false");
        }

        return mailSender;
    }
}
