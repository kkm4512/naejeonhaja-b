package com.example.naejeonhajab.domain.game.lol.dto.res.rift.player;

import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 팀 분할
public class RiftTeamResponseDto {
    private List<RiftPlayerRequestDto> teamA;
    private List<RiftPlayerRequestDto> teamB;
}
