package com.example.naejeonhajab.domain.game.lol.entity;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestPayloadDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
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
public class LolPlayerLines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_id", nullable = false)
    private LolPlayer player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolLine line;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolLineRole lineRole;

    public static List<LolPlayerLines> from(LolRequestPayloadDto lolRequestPayloadDto, List<LolPlayer> playerList) {
        List<LolPlayerLines> lineList = new ArrayList<>();
        for ( int i=0; i<playerList.size(); i++ ) {
            LolPlayer player = playerList.get(i);
            RiftRequestDto riftRequestDto = lolRequestPayloadDto.getRiftRequestDtos().get(i);
            for ( LolLinesDto lines : riftRequestDto.getLines() ) {
                lineList.add(
                        new LolPlayerLines(
                                null,
                                player,
                                lines.getLine(),
                                lines.getLineRole()
                        ));
            }
        }
        return lineList;
    }

    public static LolPlayerDto of(LolPlayerLines lolPlayerLines){
        return new LolPlayerDto(
            lolPlayerLines.getPlayer().getName(),
            lolPlayerLines.getPlayer().getTier(),
            null,
            lolPlayerLines.getPlayer().getMmr()
        );
    }
}

