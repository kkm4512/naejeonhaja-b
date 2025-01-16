package com.example.naejeonhajab.domain.game.dataDragon.dto;

import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDragonChampionDto {
    private String type;
    private String format;
    private String version;
    private Map<String, RiotChampionDto> data;

}

