package com.example.naejeonhajab.domain.game.lol.entity.result;

import com.example.naejeonhajab.common.enums.Outcome;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftLinesRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.result.RiftPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.result.RiftPlayerResultResultResponseDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolTeam;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolTeam team;

    @Column(nullable = false)
    private Integer mmr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Outcome outcome;

    @Column(nullable = false)
    private boolean mmrReduced;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_result_history_id")
    private LolPlayerResultHistory playerResultHistory;

    @OneToMany(mappedBy = "playerResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolResultLines> lines = new ArrayList<>();

    public LolPlayerResult(String name, LolTier tier, int mmr, Outcome outcome,LolPlayerResultHistory playerResultHistory, LolTeam team, boolean mmrReduced) {
        this.name = name;
        this.tier = tier;
        this.mmr = mmr;
        this.playerResultHistory = playerResultHistory;
        this.team = team;
        this.outcome = outcome;
        this.mmrReduced = mmrReduced;
    }

    public static List<LolPlayerResult> from(RiftPlayerResultHistoryRequestDto riftPlayerResultHistoryRequestDto, LolPlayerResultHistory playerResultHistory,LolTeam team) {
        List<LolPlayerResult> playerList = new ArrayList<>();
        if (team == LolTeam.TEAM_A) {
            for ( RiftPlayerRequestDto riftPlayerRequestDto : riftPlayerResultHistoryRequestDto.getTeamA().getTeam() ) {
                playerList.add(
                        new LolPlayerResult(
                                riftPlayerRequestDto.getName(),
                                riftPlayerRequestDto.getTier(),
                                riftPlayerRequestDto.getTier().getScore(),
                                riftPlayerResultHistoryRequestDto.getTeamA().getOutcome(),
                                playerResultHistory,
                                team,
                                riftPlayerRequestDto.isMmrReduced()
                        )
                );
            }
        }
        else {
            for ( RiftPlayerRequestDto riftPlayerRequestDto : riftPlayerResultHistoryRequestDto.getTeamB().getTeam()) {
                playerList.add(
                        new LolPlayerResult(
                                riftPlayerRequestDto.getName(),
                                riftPlayerRequestDto.getTier(),
                                riftPlayerRequestDto.getTier().getScore(),
                                riftPlayerResultHistoryRequestDto.getTeamB().getOutcome(),
                                playerResultHistory,
                                team,
                                riftPlayerRequestDto.isMmrReduced()
                        )
                );
            }
        }
        return playerList;
    }

    public static List<RiftPlayerResultResultResponseDto> of(List<LolPlayerResult> playerResults) {
        return playerResults.stream()
                .map(player -> {
                    // 각 플레이어의 라인을 별도로 처리
                    List<RiftLinesRequestDto> lolLinesDtos = player.getLines().stream()
                            .map(line -> new RiftLinesRequestDto(line.getLine(), line.getLineRole()))
                            .collect(Collectors.toList());

                    // 새로운 LolPlayerDto 생성
                    return new RiftPlayerResultResultResponseDto(
                            player.getName(),
                            player.getTier(),
                            player.getOutcome(),
                            player.getTeam(),
                            lolLinesDtos,
                            player.getMmr(),
                            player.isMmrReduced()
                    );
                })
                .collect(Collectors.toList());
    }

}
