package com.example.naejeonhajab.domain.game.riot.dto;

import com.example.naejeonhajab.domain.game.riot.entity.RiotAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotAccountDto {
    private String puuid;
    private String gameName;
    private String tagLine;

    public static RiotAccountDto of(RiotAccount riotAccount) {
        return new RiotAccountDto(
                riotAccount.getPuuid(),
                riotAccount.getGameName(),
                riotAccount.getTagLine()
        );
    }


}
