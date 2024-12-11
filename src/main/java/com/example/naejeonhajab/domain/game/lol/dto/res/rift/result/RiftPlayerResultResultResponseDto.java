package com.example.naejeonhajab.domain.game.lol.dto.res.rift.result;

import com.example.naejeonhajab.common.enums.Outcome;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftLinesRequestDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolTeam;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 클아이언트 요청으로 들어온 RiftRequestDto -> LolPlayer로 변경
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftPlayerResultResultResponseDto {
    String name;
    LolTier tier;
    Outcome outcome;
    LolTeam team;
    List<RiftLinesRequestDto> lines;
    int mmr;
    boolean mmrReduced;
}
