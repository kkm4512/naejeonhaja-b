package com.example.naejeonhajab.domain.game.riot.entity;

import com.example.naejeonhajab.domain.game.riot.dto.RiotAccountDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String puuid;
    private String gameName;
    private String tagLine;

    public RiotAccount(String puuid, String gameName, String tagLine) {
        this.puuid = puuid;
        this.gameName = gameName;
        this.tagLine = tagLine;
    }

    public static RiotAccount of(RiotAccountDto riotAccountDto) {
        return new RiotAccount(
                riotAccountDto.getPuuid(),
                riotAccountDto.getGameName(),
                riotAccountDto.getTagLine()
        );
    }
}
