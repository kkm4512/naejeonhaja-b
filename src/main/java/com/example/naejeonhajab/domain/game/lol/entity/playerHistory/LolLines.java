package com.example.naejeonhajab.domain.game.lol.entity.playerHistory;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolPlayerHistoryRequestDto;
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
public class LolLines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LolLine line;

    @Enumerated(EnumType.STRING)
    private LolLineRole lineRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_id")
    private LolPlayer player;

    public LolLines(LolLine line, LolLineRole lineRole, LolPlayer player) {
        this.line = line;
        this.lineRole = lineRole;
        this.player = player;
    }
    public static List<LolLines> from(LolPlayerHistoryRequestDto riftPlayerHistoryRequestDto, List<LolPlayer> playerList) {
        List<LolLines> lineList = new ArrayList<>();
        for ( int i=0; i<playerList.size(); i++ ) {
            LolPlayer player = playerList.get(i);
            LolPlayerDto riftPlayerRequestDto = riftPlayerHistoryRequestDto.getLolPlayerDtos().get(i);
            for ( LolLinesDto lines : riftPlayerRequestDto.getLines() ) {
                lineList.add(
                        new LolLines(
                                lines.getLine(),
                                lines.getLineRole(),
                                player
                        ));
            }
        }
        return lineList;
    }


    public static LolPlayerDto of(LolLines lolPlayerLines){
        return new LolPlayerDto(
            lolPlayerLines.getPlayer().getName(),
            lolPlayerLines.getPlayer().getTier(),
            null,
            null,
                null
        );
    }
}

