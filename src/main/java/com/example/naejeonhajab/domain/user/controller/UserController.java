package com.example.naejeonhajab.domain.user.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.user.dto.req.SigninRequestDto;
import com.example.naejeonhajab.domain.user.dto.req.SignupRequestDto;
import com.example.naejeonhajab.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody SignupRequestDto dto){
        userService.signUp(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }

    @PostMapping("/signin")
    public ApiResponse<Void> signin(@RequestBody SigninRequestDto dto, HttpServletResponse response){
        userService.signin(dto,response);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }
}
