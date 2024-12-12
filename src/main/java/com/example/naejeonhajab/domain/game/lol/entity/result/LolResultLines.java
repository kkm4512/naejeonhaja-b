package com.example.naejeonhajab.domain.game.lol.entity.result;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftTeamResultDto;
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

    public static List<LolResultLines> from(RiftTeamResultDto riftTeamResultRequestDto, List<LolPlayerResult> playerResults) {
        List<LolResultLines> lineList = new ArrayList<>();

        for (int i = 0; i < playerResults.size(); i++) {
            LolPlayerResult playerResult = playerResults.get(i);
            RiftPlayerDto riftPlayerRequestDto = riftTeamResultRequestDto.getTeam().get(i);

            for (RiftLinesDto lines : riftPlayerRequestDto.getLines()) {
                lineList.add(
                        new LolResultLines(
                                lines.getLine(),
                                lines.getLineRole(),
                                playerResult
                        )
                );
            }
        }

        return lineList;
    }


}

