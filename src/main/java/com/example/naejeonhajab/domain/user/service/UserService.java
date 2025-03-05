package com.example.naejeonhajab.domain.user.service;

import com.example.naejeonhajab.common.exception.UserException;
import com.example.naejeonhajab.common.response.enums.*;
import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import com.example.naejeonhajab.domain.user.dto.req.*;
import com.example.naejeonhajab.domain.user.entity.User;
import com.example.naejeonhajab.domain.user.repository.UserRepository;
import com.example.naejeonhajab.security.JwtDto;
import com.example.naejeonhajab.security.JwtManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.example.naejeonhajab.common.response.enums.UserApiResponse.INVALID_CREDENTIALS;
import static com.example.naejeonhajab.common.response.enums.UserApiResponse.USER_NOT_FOUND;
import static com.example.naejeonhajab.security.JwtManager.AUTHORIZATION_HEADER;

@Service
@Slf4j(topic = "UserService")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pe;
    private final JwtManager jm;
    @Value("${variables.domain:}")
    public String domain;
    private final Random random = new Random();
    private final EmailService emailService;

    @Transactional
    public void signUp(SignupRequestDto dto) {
        existsByEmail(dto.getEmail());

        String encodedPassword = pe.encode(dto.getPassword());

        User user = User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .userRole(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
    }



    @Transactional
    public String signin(SigninRequestDto dto, HttpServletResponse response) {
        User user = findByEmail(dto.getEmail());

        if (!pe.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(INVALID_CREDENTIALS);
        }

        return jm.generateJwt(JwtDto.of(user));
    }

    @Transactional
    public void sendCode(SendVerificationCodeDto dto) {
        User user = findByEmail(dto.getEmail());
        String code = String.format("%06d", random.nextInt(999999));
        user.updateCode(code);
        emailService.sendEmail(dto.getEmail(), "내전 하자 비밀번호 찾기, 이메일 인증 코드 입니다", "인증 코드 : " + code);
    }

    public void verifyCode(VerificationCodeDto dto){
        User user = findByEmail(dto.getEmail());
        user.verifyCode(dto.getCode());
    }


    public void updatePassword(UpdatePasswordDto dto) {
        User user = findByEmail(dto.getEmail());
        String encodedPassword = pe.encode(dto.getPassword());
        user.updatePassword(encodedPassword);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    public void existsByEmail(String email){
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserApiResponse.EMAIL_ALREADY_EXISTS);
        }
    }
}

