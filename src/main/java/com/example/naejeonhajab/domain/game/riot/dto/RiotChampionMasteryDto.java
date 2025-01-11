package com.example.naejeonhajab.domain.game.riot.dto;

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
}
