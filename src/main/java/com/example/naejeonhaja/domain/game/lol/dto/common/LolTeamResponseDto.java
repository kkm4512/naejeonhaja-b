package com.example.naejeonhaja.domain.game.lol.dto.common;

import com.example.naejeonhaja.domain.game.lol.enums.LolBalanceEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LolTeamResponseDto {
    private LolBalanceEnum balance;
    private int teamATotalMmr;
    private int teamBTotalMmr;
    private List<LolPlayerDto> teamA;
    private List<LolPlayerDto> teamB;

    public static LolTeamResponseDto of(List<LolPlayerDto> teamA, List<LolPlayerDto> teamB, LolBalanceEnum balance) {
        int teamATotalMmr = teamA.stream().mapToInt(LolPlayerDto::getMmr).sum();
        int teamBTotalMmr = teamB.stream().mapToInt(LolPlayerDto::getMmr).sum();
        return new LolTeamResponseDto(balance, teamATotalMmr, teamBTotalMmr, teamA, teamB);
    }
}
