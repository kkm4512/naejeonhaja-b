package com.example.naejeonhajab.domain.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SigninRequestDto {
    private String email;
    private String password;
}
