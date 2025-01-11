package com.example.naejeonhajab.domain.game.riot.dto;

import com.example.naejeonhajab.domain.game.riot.enums.LolRankType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotLeagueDto {
    private String leagueId;
    private LolRankType queueType;
    private String tier;
    private String rank;
    private String summonerId;
    private int leaguePoints;
    private int wins;
    private int losses;
}
