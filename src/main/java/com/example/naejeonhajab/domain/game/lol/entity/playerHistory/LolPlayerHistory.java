package com.example.naejeonhajab.domain.game.lol.entity.playerHistory;

import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryRequestDto;
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
public class LolPlayerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String playerHistoryTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolType type;

    @OneToMany(mappedBy = "playerHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolPlayer> players = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public LolPlayerHistory(User user, String playerHistoryTitle, LolType type) {
        this.user = user;
        this.playerHistoryTitle = playerHistoryTitle;
        this.type = type;
    }

    public static LolPlayerHistory from (LolPlayerHistoryRequestDto dto, User user, LolType type) {
        return new LolPlayerHistory(
                user,
                dto.getPlayerHistoryTitle(),
                type
        );
    }

    public void updatePlayerHistoryTitle(String playerHistoryTitle) {
        this.playerHistoryTitle = playerHistoryTitle;
    }

}

