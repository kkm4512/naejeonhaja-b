package com.example.naejeonhajab.domain.game.riot.entity;

import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RiotChampion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long championId;
    @Column(nullable = false)
    private String version;
    @Column(nullable = false)
    private String id;
    @Column(nullable = false, name = "`key`")
    private String key;
    @Column(nullable = false, name = "`name`")
    private String name;

    @ManyToOne
    private RiotPlayer riotPlayer;

    @Embedded
    private Image image;

    public RiotChampion(String version, String id, String key, String name, Image image, RiotPlayer riotPlayer) {
        this.version = version;
        this.id = id;
        this.key = key;
        this.name = name;
        this.image = image;
        this.riotPlayer = riotPlayer;
    }

    public RiotChampion(String version, String id, String key, String name, Image image) {
        this.version = version;
        this.id = id;
        this.key = key;
        this.name = name;
        this.image = image;
    }

    public static RiotChampion from(RiotChampionDto dataDragonChampionDto) {
        return new RiotChampion(
                dataDragonChampionDto.getVersion(),
                dataDragonChampionDto.getId(),
                dataDragonChampionDto.getKey(),
                dataDragonChampionDto.getName(),
                Image.of(dataDragonChampionDto.getImage())
        );
    }

    public static List<RiotChampion> from(List<RiotChampionDto> championDtos, RiotPlayer riotPlayer) {
        return championDtos.stream().map(dto -> new RiotChampion(
                dto.getVersion(),
                dto.getId(),
                dto.getKey(),
                dto.getName(),
                Image.of(dto.getImage()),
                riotPlayer
        )).toList();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Image {
        @Column(nullable = false)
        private String full;
        @Column(nullable = false)
        private String sprite;
        @Column(nullable = false, name = "`group`")
        private String group;
        @Column(nullable = false)
        private int x;
        @Column(nullable = false)
        private int y;
        @Column(nullable = false)
        private int w;
        @Column(nullable = false)
        private int h;

        public static Image of(RiotChampionDto.ImageDto imageDto) {
            return new Image(
                    imageDto.getFull(),
                    imageDto.getSprite(),
                    imageDto.getGroup(),
                    imageDto.getX(),
                    imageDto.getY(),
                    imageDto.getW(),
                    imageDto.getH()
            );
        }
    }
}

