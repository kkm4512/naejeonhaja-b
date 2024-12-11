package com.example.naejeonhajab.domain.game.lol.entity.result;

import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftLinesRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.result.RiftPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import com.example.naejeonhajab.domain.game.lol.enums.LolTeam;
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
public class LolResultLines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolLine line;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LolLineRole lineRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lol_player_result_id")
    private LolPlayerResult playerResult;

    public LolResultLines(LolLine line, LolLineRole lineRole, LolPlayerResult playerResult) {
        this.line = line;
        this.lineRole = lineRole;
        this.playerResult = playerResult;
    }

    public static List<LolResultLines> from(RiftPlayerResultHistoryRequestDto riftPlayerResultHistoryRequestDto, List<LolPlayerResult> playerResults, LolTeam team) {
        List<LolResultLines> lineList = new ArrayList<>();
        if (team == LolTeam.TEAM_A) {
            for ( int i=0; i<playerResults.size(); i++ ) {
                LolPlayerResult playerResult = playerResults.get(i);
                RiftPlayerRequestDto riftPlayerRequestDto = riftPlayerResultHistoryRequestDto.getTeamA().getTeam().get(i);
                for ( RiftLinesRequestDto lines : riftPlayerRequestDto.getLines() ) {
                    lineList.add(
                            new LolResultLines(
                                    lines.getLine(),
                                    lines.getLineRole(),
                                    playerResult
                            ));
                }
            }
        }
        else {
            for ( int i=0; i<playerResults.size(); i++ ) {
                LolPlayerResult playerResult = playerResults.get(i);
                RiftPlayerRequestDto riftPlayerRequestDto = riftPlayerResultHistoryRequestDto.getTeamB().getTeam().get(i);
                for ( RiftLinesRequestDto lines : riftPlayerRequestDto.getLines() ) {
                    lineList.add(
                            new LolResultLines(
                                    lines.getLine(),
                                    lines.getLineRole(),
                                    playerResult
                            ));
                }
            }
        }
        return lineList;
    }
}

