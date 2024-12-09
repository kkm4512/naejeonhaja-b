package com.example.naejeonhajab.domain.game.lol.dto.res.rift;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolTeamDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.common.LolResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 5:5 내전 결과 보여주는 데이터
public class RiftResponseDto implements LolResponseDto {
    private List<LolPlayerDto> teamA;
    private List<LolPlayerDto> teamB;

    public static RiftResponseDto of(LolTeamDto team) {
        return new RiftResponseDto(team.getTeamA(), team.getTeamB());
    }
}

