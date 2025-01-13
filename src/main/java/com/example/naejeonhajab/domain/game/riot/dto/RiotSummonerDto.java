package com.example.naejeonhajab.domain.game.riot.dto;

import com.example.naejeonhajab.domain.game.riot.entity.RiotSummoner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotSummonerDto {
    private String id;
    private String accountId;
    private String puuid;
    private int profileIconId;
    private long revisionDate;
    private int summonerLevel;

    public static RiotSummonerDto of(RiotSummoner riotSummoner) {
        return new RiotSummonerDto(
                riotSummoner.getId(),
                riotSummoner.getAccountId(),
                riotSummoner.getPuuid(),
                riotSummoner.getProfileIconId(),
                riotSummoner.getRevisionDate(),
                riotSummoner.getSummonerLevel()
        );
    }
}
