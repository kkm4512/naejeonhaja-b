package com.example.naejeonhajab.domain.game.lol.entity.result;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResultDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolLinesDto;
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

    @Column(nullable = false)
    private Integer mmr;

    private boolean mmrReduced;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_result_outcome_id")
    private LolPlayerResultOutcome playerResultOutcome;

    @OneToMany(mappedBy = "playerResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolResultLines> lines = new ArrayList<>();

    // 소환사의 협곡
    public LolPlayerResult(String name, LolTier tier, int mmr,LolPlayerResultOutcome playerResultOutcome, boolean mmrReduced) {
        this.name = name;
        this.tier = tier;
        this.mmr = mmr;
        this.playerResultOutcome = playerResultOutcome;
        this.mmrReduced = mmrReduced;
    }

    // 칼바람
    public LolPlayerResult(String name, LolTier tier, Integer mmr,LolPlayerResultOutcome playerResultOutcome) {
        this.name = name;
        this.tier = tier;
        this.mmr = mmr;
        this.playerResultOutcome = playerResultOutcome;
    }

    public static List<LolPlayerResult> fromRiftTeamResultDtoAndLolPlayerResultOutcome(com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResultDto dto, LolPlayerResultOutcome lolPlayerResultOutcome) {
        List<LolPlayerResult> playerList = new ArrayList<>();

        for (LolPlayerDto riftPlayerRequestDto : dto.getTeam()) {
            playerList.add(
                    new LolPlayerResult(
                            riftPlayerRequestDto.getName(),
                            riftPlayerRequestDto.getTier(),
                            riftPlayerRequestDto.getTier().getScore(),
                            lolPlayerResultOutcome,
                            riftPlayerRequestDto.getMmrReduced()
                    )
            );
        }
        return playerList;
    }

    public static List<LolPlayerResult> fromAbyssTeamResultDtoAndLolPlayerResultOutcome(LolTeamResultDto dto, LolPlayerResultOutcome lolPlayerResultOutcome) {
        List<LolPlayerResult> playerList = new ArrayList<>();

        for (LolPlayerDto riftPlayerRequestDto : dto.getTeam()) {
            playerList.add(
                    new LolPlayerResult(
                            riftPlayerRequestDto.getName(),
                            riftPlayerRequestDto.getTier(),
                            riftPlayerRequestDto.getTier().getScore(),
                            lolPlayerResultOutcome
                    )
            );
        }
        return playerList;
    }


    public static List<LolPlayerDto> of(List<LolPlayerResult> playerResults) {
        return playerResults.stream()
                .map(player -> {
                    // 각 플레이어의 라인을 별도로 처리
                    List<LolLinesDto> lolLinesDtos = player.getLines().stream()
                            .map(line -> new LolLinesDto(line.getLine(), line.getLineRole()))
                            .collect(Collectors.toList());

                    // 새로운 LolPlayerDto 생성
                    return new LolPlayerDto(
                            player.getName(),
                            player.getTier(),
                            lolLinesDtos,
                            player.getMmr(),
                            player.isMmrReduced()
                    );
                })
                .collect(Collectors.toList());
    }

}
