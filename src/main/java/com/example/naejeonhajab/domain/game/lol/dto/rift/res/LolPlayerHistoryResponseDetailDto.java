package com.example.naejeonhajab.domain.game.lol.dto.rift.res;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 제목 + 10명의 유저 의 정보 전부있음
public class LolPlayerHistoryResponseDetailDto {
    private String playerHistoryTitle;
    private List<RiftPlayerDto> riftPlayerDtos;

    public static LolPlayerHistoryResponseDetailDto of(LolPlayerHistory lolPlayerHistory){
        List<LolPlayer> players = lolPlayerHistory.getPlayers();
        List<RiftPlayerDto> playerDtos = LolPlayer.of(players);
        return new LolPlayerHistoryResponseDetailDto(
                lolPlayerHistory.getPlayerHistoryTitle(),
                playerDtos
        );
    }
}
