package com.example.naejeonhajab.domain.game.lol.entity;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestPayloadDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftRequestDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_history_id", nullable = false)
    private LolPlayerHistory playerHistory;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolTier tier;

    @Column(nullable = false)
    private int mmr;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LolPlayerLines> lines = new ArrayList<>();

    public static List<LolPlayer> from(LolRequestPayloadDto lolRequestPayloadDto, LolPlayerHistory playerHistory) {
        List<LolPlayer> playerList = new ArrayList<>();
        for ( RiftRequestDto riftRequestDto : lolRequestPayloadDto.getRiftRequestDtos() ) {
            playerList.add(
                    new LolPlayer(
                            null,
                            playerHistory,
                            riftRequestDto.getName(),
                            riftRequestDto.getTier(),
                            riftRequestDto.getTier().getScore(),
                            null
                    )
            );
        }
        return playerList;
    }

    public static List<LolPlayerDto> of(List<LolPlayer> players) {
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

