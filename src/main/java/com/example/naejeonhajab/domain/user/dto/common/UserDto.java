package com.example.naejeonhajab.domain.user.dto.common;

import com.example.naejeonhajab.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String nickname;

    public static UserDto of(User user) {
        return new UserDto(user.getId(), user.getNickname());
    }
}
