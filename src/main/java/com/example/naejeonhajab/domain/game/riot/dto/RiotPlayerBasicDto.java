package com.example.naejeonhajab.domain.game.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiotPlayerBasicDto {
    private RiotAccountDto riotAccountDto;
    private RiotSummonerDto riotSummonerDto;
    private RiotLeagueDto riotLeagueDto;
}
