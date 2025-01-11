package com.example.naejeonhajab.domain.game.dataDragon.dto;

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
    private Map<String, ChampionDto> data;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChampionDto {
        private String version;
        private String id;
        private String key;
        private String name;
        private ImageDto image;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageDto {
        private String full;
        private String sprite;
        private String group;
        private int x;
        private int y;
        private int w;
        private int h;
    }

}

