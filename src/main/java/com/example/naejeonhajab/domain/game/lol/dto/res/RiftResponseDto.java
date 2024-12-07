package com.example.naejeonhajab.domain.game.lol.dto.res;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftResponseDto implements LolResponseDto {
    private List<LolPlayer> teamA;
    private List<LolPlayer> teamB;

    public static RiftResponseDto of(LolTeam team) {
        return new RiftResponseDto(team.getTeamA(), team.getTeamB());
    }
}

