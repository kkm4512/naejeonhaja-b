package com.example.naejeonhajab.domain.game.lol.entity;

import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftLinesRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftPlayerRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftPlayerHistoryRequestDto;
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
    private List<LolLines> lines = new ArrayList<>();

    public static List<LolPlayer> from(RiftPlayerHistoryRequestDto lolRequestPayloadDto, LolPlayerHistory playerHistory) {
        List<LolPlayer> playerList = new ArrayList<>();
        for ( RiftPlayerRequestDto riftPlayerRequestDto : lolRequestPayloadDto.getRiftPlayerRequestDtos() ) {
            playerList.add(
                    new LolPlayer(
                            null,
                            playerHistory,
                            riftPlayerRequestDto.getName(),
                            riftPlayerRequestDto.getTier(),
                            riftPlayerRequestDto.getTier().getScore(),
                            null
                    )
            );
        }
        return playerList;
    }

    public static List<RiftPlayerRequestDto> of(List<LolPlayer> players) {
        return players.stream()
                .map(player -> {
                    // 각 플레이어의 라인을 별도로 처리
                    List<RiftLinesRequestDto> lolLinesDtos = player.getLines().stream()
                            .map(line -> new RiftLinesRequestDto(line.getLine(), line.getLineRole()))
                            .collect(Collectors.toList());

                    // 새로운 LolPlayerDto 생성
                    return new RiftPlayerRequestDto(
                            player.getName(),
                            player.getTier(),
                            lolLinesDtos
                    );
                })
                .collect(Collectors.toList());
    }


}

