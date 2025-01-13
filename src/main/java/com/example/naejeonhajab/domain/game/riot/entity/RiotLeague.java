package com.example.naejeonhajab.domain.game.riot.entity;

import com.example.naejeonhajab.domain.game.riot.dto.RiotLeagueDto;
import com.example.naejeonhajab.domain.game.riot.enums.LolRankType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotLeague {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String leagueId;
    @Column(nullable = false)
    private LolRankType queueType;
    @Column(nullable = false)
    private String tier;
    @Column(nullable = false, name = "`rank`")
    private String rank;
    @Column(nullable = false)
    private String summonerId;

    public RiotLeague(String leagueId, LolRankType queueType, String tier, String rank, String summonerId, int leaguePoints, int wins, int losses) {
        this.leagueId = leagueId;
        this.queueType = queueType;
        this.tier = tier;
        this.rank = rank;
        this.summonerId = summonerId;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
    }

    public static RiotLeague of(RiotLeagueDto riotLeagueDto) {
        return new RiotLeague(
                riotLeagueDto.getLeagueId(),
                riotLeagueDto.getQueueType(),
                riotLeagueDto.getTier(),
                riotLeagueDto.getRank(),
                riotLeagueDto.getSummonerId(),
                riotLeagueDto.getLeaguePoints(),
                riotLeagueDto.getWins(),
                riotLeagueDto.getLosses()
        );
    }

    @Column(nullable = false)
    private int leaguePoints;
    @Column(nullable = false)
    private int wins;
    @Column(nullable = false)
    private int losses;


}
