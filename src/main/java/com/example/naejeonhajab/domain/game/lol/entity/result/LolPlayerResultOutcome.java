package com.example.naejeonhajab.domain.game.lol.entity.result;

import com.example.naejeonhajab.common.enums.Outcome;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftTeamResultDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerResultOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Outcome outcome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_result_history_id")
    private LolPlayerResultHistory playerResultHistory;

    @OneToMany(mappedBy = "playerResultOutcome", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolPlayerResult> playerResults = new ArrayList<>();

    public LolPlayerResultOutcome(Outcome outcome, LolPlayerResultHistory playerResultHistory) {
        this.outcome = outcome;
        this.playerResultHistory = playerResultHistory;
    }

    public static LolPlayerResultOutcome from(RiftTeamResultDto riftTeamResultRequestDto, LolPlayerResultHistory playerResultHistory) {
        return new LolPlayerResultOutcome(
                riftTeamResultRequestDto.getOutcome(),
                playerResultHistory
        );
    }
}
