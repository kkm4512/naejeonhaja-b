package com.example.naejeonhajab.security;

import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import com.example.naejeonhajab.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    private Long userId;
    private String nickname;
    private String email;
    private UserRole userRole;

    public static JwtDto of (User user) {
        return new JwtDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole()
        );
    }
}
