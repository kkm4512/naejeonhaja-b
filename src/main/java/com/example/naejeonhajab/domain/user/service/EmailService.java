package com.example.naejeonhajab.domain.user.service;

import com.example.naejeonhajab.common.exception.UserException;
import com.example.naejeonhajab.domain.dto.common.MailProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.naejeonhajab.common.response.enums.UserApiResponse.NOT_MATCH_EMAIL;

@Service
public class EmailService {

    private final Map<String, JavaMailSender> mailSenders;

    private JavaMailSender jms; // 동적으로 설정될 JavaMailSender
    private final MailProperties mailProperties;

    public EmailService(
            @Qualifier("naverMailSender") JavaMailSender naverMailSender,
            @Qualifier("gmailMailSender") JavaMailSender gmailMailSender,
            @Qualifier("kakaoMailSender") JavaMailSender kakaoMailSender,
            MailProperties mailProperties
    ) {
        // 도메인과 JavaMailSender를 매핑
        this.mailSenders = Map.of(
                "naver.com", naverMailSender,
                "gmail.com", gmailMailSender,
                "kakao.com", kakaoMailSender
        );
        this.mailProperties = mailProperties; // Spring 컨텍스트에서 관리되는 MailProperties를 사용
    }

    public void sendEmail(String email, String subject, String body) {
        // 이메일 도메인 추출
        String domain = extractDomain(email);
        String from = switch (domain) {
            case "naver.com" -> mailProperties.getNaver().getEmail();
            case "gmail.com" -> mailProperties.getGmail().getEmail();
            case "kakao.com" -> mailProperties.getKakao().getEmail();
            default -> throw new UserException(NOT_MATCH_EMAIL);
        };

        // 도메인에 따라 적절한 JavaMailSender 설정
        jms = mailSenders.get(domain);
        if (jms == null) {
            throw new UserException(NOT_MATCH_EMAIL);
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 발신자 설정
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        jms.send(message); // 설정된 JavaMailSender로 이메일 전송
    }

    private String extractDomain(String email) {
        // 이메일에서 도메인 추출
        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            throw new UserException(NOT_MATCH_EMAIL);
        }
        return email.substring(atIndex + 1);
    }
}

