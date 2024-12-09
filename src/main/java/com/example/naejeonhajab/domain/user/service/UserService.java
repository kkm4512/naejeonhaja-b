package com.example.naejeonhajab.domain.user.service;

import com.example.naejeonhajab.common.exception.UserException;
import com.example.naejeonhajab.common.response.enums.*;
import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import com.example.naejeonhajab.domain.user.dto.req.SigninRequestDto;
import com.example.naejeonhajab.domain.user.dto.req.SignupRequestDto;
import com.example.naejeonhajab.domain.user.entity.User;
import com.example.naejeonhajab.domain.user.repository.UserRepository;
import com.example.naejeonhajab.security.JwtDto;
import com.example.naejeonhajab.security.JwtManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.naejeonhajab.common.response.enums.UserApiResponse.INVALID_CREDENTIALS;
import static com.example.naejeonhajab.common.response.enums.UserApiResponse.USER_NOT_FOUND;
import static com.example.naejeonhajab.security.JwtManager.AUTHORIZATION_HEADER;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pe;
    private final JwtManager jm;

    @Transactional
    public void signUp(SignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserException(UserApiResponse.EMAIL_ALREADY_EXISTS);
        }

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
    public void signin(SigninRequestDto dto, HttpServletResponse response) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if (!pe.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(INVALID_CREDENTIALS);
        }

        String jwt = jm.generateJwt(JwtDto.of(user));

        ResponseCookie cookie = ResponseCookie.from(AUTHORIZATION_HEADER, jwt)
                .path("/")                  // 쿠키의 경로 설정
                .maxAge(7 * 24 * 60 * 60)   // 쿠키 만료 시간 (7일)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}

