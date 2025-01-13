package com.example.naejeonhajab.domain.game.riot.entity;

import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionMasteryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotChampionMastery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String puuid;
    private int championId;
    private int championLevel;
    private int championPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    private RiotPlayer riotPlayer;

    public RiotChampionMastery(String puuid, int championId, int championLevel, int championPoints, RiotPlayer riotPlayer) {
        this.puuid = puuid;
        this.championId = championId;
        this.championLevel = championLevel;
        this.championPoints = championPoints;
        this.riotPlayer = riotPlayer;
    }

    public static RiotChampionMastery from(RiotChampionMasteryDto riotChampionMasteryDto, RiotPlayer riotPlayer) {
        return new RiotChampionMastery(
                riotChampionMasteryDto.getPuuid(),
                riotChampionMasteryDto.getChampionId(),
                riotChampionMasteryDto.getChampionLevel(),
                riotChampionMasteryDto.getChampionPoints(),
                riotPlayer
        );

    }

    public static List<RiotChampionMastery> from(List<RiotChampionMasteryDto> riotChampionMasteryDtos, RiotPlayer riotPlayer) {
        return riotChampionMasteryDtos.stream().map(dto -> RiotChampionMastery.from(dto,riotPlayer))
                .toList();
    }
}
