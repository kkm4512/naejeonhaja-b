package com.example.naejeonhajab.domain.game.lol.entity.resultHistory;

import com.example.naejeonhajab.domain.game.lol.dto.req.LolPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class LolPlayerResultHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String playerResultHistoryTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolType type;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "playerResultHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolPlayerResultOutcome> playerResultOutcomes = new ArrayList<>();

    public LolPlayerResultHistory(User user, String playerResultHistoryTitle, LolType type) {
        this.user = user;
        this.playerResultHistoryTitle = playerResultHistoryTitle;
        this.type = type;
    }

    public static LolPlayerResultHistory from(LolPlayerResultHistoryRequestDto dto, User user, LolType type) {
        return new LolPlayerResultHistory(
                user,
                dto.getPlayerResultHistoryTitle(),
                type
        );
    }
}

