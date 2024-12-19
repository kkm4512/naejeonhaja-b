package com.example.naejeonhajab.domain.game.lol.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 팀 분할
public class LolTeamResponseDto {
    private List<LolPlayerDto> teamA;
    private List<LolPlayerDto> teamB;

    public static LolTeamResponseDto of(List<LolPlayerDto> teamA, List<LolPlayerDto> teamB) {
        return new LolTeamResponseDto(
                teamA,
                teamB
        );
    }
}
