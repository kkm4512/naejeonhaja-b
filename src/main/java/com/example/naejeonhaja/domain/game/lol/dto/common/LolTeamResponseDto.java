package com.example.naejeonhaja.domain.game.lol.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LolTeamResponseDto {
    private List<LolPlayerDto> teamA;
    private List<LolPlayerDto> teamB;

    public static LolTeamResponseDto of(List<LolPlayerDto> teamA, List<LolPlayerDto> teamB) {
        return new LolTeamResponseDto(teamA, teamB);
    }
}
