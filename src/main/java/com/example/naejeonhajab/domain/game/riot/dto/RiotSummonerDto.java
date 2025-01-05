package com.example.naejeonhajab.domain.game.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotSummonerDto {
    private String id;
    private String accountId;
    private String puuid;
    private int profileIconId;
    private long revisionDate;
    private int summonerLevel;
}
