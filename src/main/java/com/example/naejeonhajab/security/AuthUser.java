package com.example.naejeonhajab.security;

import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser implements Principal, Serializable {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String email, String nickname, UserRole userRole) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}
