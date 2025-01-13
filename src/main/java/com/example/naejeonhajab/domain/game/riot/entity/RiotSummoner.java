package com.example.naejeonhajab.domain.game.riot.entity;

import com.example.naejeonhajab.domain.game.riot.dto.RiotSummonerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotSummoner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riotSummonerId;

    private String id;
    private String puuid;
    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private int summonerLevel;

    public RiotSummoner(String id, String accountId, int profileIconId, long revisionDate, int summonerLevel) {
        this.id = id;
        this.accountId = accountId;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }

    public static RiotSummoner of(RiotSummonerDto riotSummonerDto) {
        return new RiotSummoner(
                riotSummonerDto.getId(),
                riotSummonerDto.getAccountId(),
                riotSummonerDto.getProfileIconId(),
                riotSummonerDto.getRevisionDate(),
                riotSummonerDto.getSummonerLevel()
        );
    }
}
