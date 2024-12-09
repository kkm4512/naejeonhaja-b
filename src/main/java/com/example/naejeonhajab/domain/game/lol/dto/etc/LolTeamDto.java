package com.example.naejeonhajab.domain.game.lol.dto.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 팀 분할
public class LolTeamDto {
    private List<LolPlayerDto> teamA;
    private List<LolPlayerDto> teamB;
}
