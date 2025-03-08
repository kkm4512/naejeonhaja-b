package com.example.naejeonhajab.domain.inquiry.entity;

import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayer;
import com.example.naejeonhajab.domain.inquiry.dto.InquiryRequestDto;
import com.example.naejeonhajab.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 문의 ID (PK)

    @Column(nullable = false, length = 100)
    private String title; // 문의 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 문의 내용

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 날짜

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 날짜

    public static Inquiry of (InquiryRequestDto dto, User user){
        return Inquiry.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
    }

    public void update(InquiryRequestDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.tags.clear();
    }
}

