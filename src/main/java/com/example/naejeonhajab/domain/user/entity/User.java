package com.example.naejeonhajab.domain.user.entity;

import com.example.naejeonhajab.domain.game.lol.entity.LolPlayerHistory;
import com.example.naejeonhajab.domain.user.dto.common.UserRole;
import com.example.naejeonhajab.security.AuthUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolPlayerHistory> playerHistories = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    public static User of(AuthUser authUser){
        String roleName = authUser.getAuthorities()
                .stream()
                .findFirst() // 첫 번째 권한만 사용
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalArgumentException("No authority found"));

        UserRole userRole = UserRole.valueOf(roleName); // 문자열을 UserRole Enum으로 변환
        return new User(
                authUser.getUserId(),
                authUser.getNickname(),
                authUser.getEmail(),
                null,
                userRole,
                null,
                null,
                null
        );
    }
}