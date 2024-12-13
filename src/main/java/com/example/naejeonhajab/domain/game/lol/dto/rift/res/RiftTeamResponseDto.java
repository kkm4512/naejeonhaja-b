package com.example.naejeonhajab.domain.game.lol.dto.rift.res;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 팀 분할
public class RiftTeamResponseDto {
    private List<RiftPlayerDto> teamA;
    private List<RiftPlayerDto> teamB;
}
