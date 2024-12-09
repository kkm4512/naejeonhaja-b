package com.example.naejeonhajab.domain.game.lol.dto.res.rift;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayerHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 제목 + 10명의 유저 의 정보 전부있음
public class RiftPlayerHistoryResponseDetailDto {
    private String playerHistoryTitle;
    private List<LolPlayerDto> lolPlayerDtos;

    public static RiftPlayerHistoryResponseDetailDto of(LolPlayerHistory lolPlayerHistory){
        List<LolPlayer> players = lolPlayerHistory.getPlayers();
        List<LolPlayerDto> playerDtos = LolPlayer.of(players);
        return new RiftPlayerHistoryResponseDetailDto(
                lolPlayerHistory.getPlayerHistoryTitle(),
                playerDtos
        );
    }

    public static RiftPlayerHistoryResponseDetailDto from(LolPlayerHistory lolPlayerHistory, List<LolPlayer> players) {
        List<LolPlayerDto> playerDtos = LolPlayer.of(players);
        return new RiftPlayerHistoryResponseDetailDto(
                lolPlayerHistory.getPlayerHistoryTitle(),
                playerDtos
        );
    }
}
