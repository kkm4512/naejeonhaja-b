package com.example.naejeonhajab.domain.game.riot.dto;

import com.example.naejeonhajab.domain.game.riot.entity.RiotChampion;
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
public class RiotChampionDto {
    private String version;
    private String id;
    private String key;
    private String name;
    private ImageDto image;

    public static RiotChampionDto of(RiotChampion riotChampionDto) {
        return new RiotChampionDto(
                riotChampionDto.getVersion(),
                riotChampionDto.getId(),
                riotChampionDto.getKey(),
                riotChampionDto.getName(),
                ImageDto.of(riotChampionDto.getImage())
        );
    }

    public static List<RiotChampionDto> of(List<RiotChampion> riotChampionDtos) {
        return riotChampionDtos.stream().map(RiotChampionDto::of).toList();
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

        public static ImageDto of(RiotChampion.Image image) {
            return new ImageDto(
                    image.getFull(),
                    image.getSprite(),
                    image.getGroup(),
                    image.getX(),
                    image.getY(),
                    image.getW(),
                    image.getH()
            );
        }
    }
}

