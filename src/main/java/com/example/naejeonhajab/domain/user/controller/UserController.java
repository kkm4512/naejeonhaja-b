package com.example.naejeonhajab.domain.user.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.user.dto.req.*;
import com.example.naejeonhajab.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.naejeonhajab.security.JwtManager.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequestDto dto){
        userService.signUp(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid SigninRequestDto dto, HttpServletResponse response){
        return userService.signin(dto, response);
    }

    // 비밀번호 잊었을시 유저의 이메일을 사용자로부터 받고, 그 이메일이 있는지 찾고 해당 이메일로 인증코드 보냄
    @PostMapping("/send-code")
    public ApiResponse<Void> sendCode(@RequestBody @Valid SendVerificationCodeDto dto){
        userService.sendCode(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }

    @PostMapping("/verify-code")
    public ApiResponse<Void> verifyCode(@RequestBody @Valid VerificationCodeDto dto){
        userService.verifyCode(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }

    @PutMapping("/update-password")
    public ApiResponse<Void> updatePassword(@RequestBody @Valid UpdatePasswordDto dto){
        userService.updatePassword(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }
}
