package com.example.naejeonhajab.domain.game.lol.entity.player;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.req.RiftPlayerHistoryRequestDto;
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
    public static List<LolLines> from(RiftPlayerHistoryRequestDto riftPlayerHistoryRequestDto, List<LolPlayer> playerList) {
        List<LolLines> lineList = new ArrayList<>();
        for ( int i=0; i<playerList.size(); i++ ) {
            LolPlayer player = playerList.get(i);
            RiftPlayerDto riftPlayerRequestDto = riftPlayerHistoryRequestDto.getPlayerDtos().get(i);
            for ( RiftLinesDto lines : riftPlayerRequestDto.getLines() ) {
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


    public static RiftPlayerDto of(LolLines lolPlayerLines){
        return new RiftPlayerDto(
            lolPlayerLines.getPlayer().getName(),
            lolPlayerLines.getPlayer().getTier(),
            null
        );
    }
}

