package com.example.naejeonhajab.domain.game.riot.dto;

import com.example.naejeonhajab.domain.game.riot.entity.RiotChampionMastery;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotChampionMasteryDto {
    private String puuid;
    private int championId;
    private int championLevel;
    private int championPoints;

    public static RiotChampionMasteryDto of(RiotChampionMastery championMastery) {
        return new RiotChampionMasteryDto(
                championMastery.getPuuid(),
                championMastery.getChampionId(),
                championMastery.getChampionLevel(),
                championMastery.getChampionPoints()
        );
    }

    public static List<RiotChampionMasteryDto> of(List<RiotChampionMastery> riotChampionMasteries) {
        return riotChampionMasteries.stream().map(RiotChampionMasteryDto::of).toList();
    }
}
