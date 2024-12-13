package com.example.naejeonhajab.domain.game.lol.dto.rift.res;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
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
    @Valid
    @Size
    private List<RiftPlayerDto> playerDtos;

    public static RiftPlayerHistoryResponseDetailDto of(LolPlayerHistory lolPlayerHistory){
        List<LolPlayer> players = lolPlayerHistory.getPlayers();
        List<RiftPlayerDto> playerDtos = LolPlayer.toRiftPlayerDtos(players);
        return new RiftPlayerHistoryResponseDetailDto(
                lolPlayerHistory.getPlayerHistoryTitle(),
                playerDtos
        );
    }
}
