package com.example.naejeonhajab.domain.game.lol.entity.player;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.req.LolPlayerHistoryRequestDto;
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

    public static List<LolPlayer> from(LolPlayerHistoryRequestDto lolRequestPayloadDto, LolPlayerHistory playerHistory) {
        List<LolPlayer> playerList = new ArrayList<>();
        for ( RiftPlayerDto riftPlayerRequestDto : lolRequestPayloadDto.getRiftPlayerDtos() ) {
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

    public static List<RiftPlayerDto> of(List<LolPlayer> players) {
        return players.stream()
                .map(player -> {
                    // 각 플레이어의 라인을 별도로 처리
                    List<RiftLinesDto> lolLinesDtos = player.getLines().stream()
                            .map(line -> new RiftLinesDto(line.getLine(), line.getLineRole()))
                            .collect(Collectors.toList());

                    // 새로운 LolPlayerDto 생성
                    return new RiftPlayerDto(
                            player.getName(),
                            player.getTier(),
                            lolLinesDtos
                    );
                })
                .collect(Collectors.toList());
    }


}

