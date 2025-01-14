package com.example.naejeonhajab.domain.game.riot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotChampionDto {
    private String version;
    private String id;
    private String key;
    private String name;
    private ImageDto image;


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

