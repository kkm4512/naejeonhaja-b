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
public class TftResponseDto implements LolResponseDto {
    private List<LolPlayer> teamA;
    private List<LolPlayer> teamB;

    public static TftResponseDto of(LolTeam team) {
        return new TftResponseDto(team.getTeamA(), team.getTeamB());
    }
}

