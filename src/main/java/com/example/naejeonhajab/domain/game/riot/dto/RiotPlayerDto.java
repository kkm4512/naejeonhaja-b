package com.example.naejeonhajab.domain.game.riot.dto;

import com.example.naejeonhajab.domain.game.dataDragon.dto.DataDragonChampionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotPlayerDto {
    private RiotAccountDto riotAccountDto;
    private RiotSummonerDto riotSummonerDto;
    private List<RiotChampionMasteryDto> riotChampionMasteryDtos;
    private RiotLeagueDto riotLeagueDto;
    private List<DataDragonChampionDto.ChampionDto> championDtos;
}
