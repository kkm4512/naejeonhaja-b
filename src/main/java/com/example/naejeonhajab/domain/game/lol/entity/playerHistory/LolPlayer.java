package com.example.naejeonhajab.domain.game.lol.entity.playerHistory;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryRequestDto;
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
public class LolPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolTier tier;

    @Column(nullable = false)
    private int mmr;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolLines> lines = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_history_id")
    private LolPlayerHistory playerHistory;

    public LolPlayer(String name, LolTier tier, int mmr, LolPlayerHistory playerHistory) {
        this.name = name;
        this.tier = tier;
        this.mmr = mmr;
        this.playerHistory = playerHistory;
    }


    public static List<LolPlayer> from(LolPlayerHistoryRequestDto dto, LolPlayerHistory playerHistory) {
        List<LolPlayer> playerList = new ArrayList<>();
        for ( LolPlayerDto riftPlayerRequestDto : dto.getLolPlayerDtos() ) {
            playerList.add(
                    new LolPlayer(
                            riftPlayerRequestDto.getName(),
                            riftPlayerRequestDto.getTier(),
                            riftPlayerRequestDto.getTier().getScore(),
                            playerHistory
                    )
            );
        }
        return playerList;
    }

    public static List<LolPlayerDto> to(List<LolPlayer> players) {
        return players.stream()
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
                            player.getMmr()
                    );
                })
                .collect(Collectors.toList());
    }

}

