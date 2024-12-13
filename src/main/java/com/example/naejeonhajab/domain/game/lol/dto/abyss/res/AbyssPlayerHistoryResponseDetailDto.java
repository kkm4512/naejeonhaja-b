package com.example.naejeonhajab.domain.game.lol.dto.abyss.res;

import com.example.naejeonhajab.domain.game.lol.dto.abyss.common.AbyssPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.res.RiftPlayerHistoryResponseDetailDto;
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
public class AbyssPlayerHistoryResponseDetailDto {
    private String playerHistoryTitle;
    @Valid
    @Size(max = 10, message = "최대 10명의 플레이어만 들어올 수 있습니다.")
    private List<AbyssPlayerDto> playerDtos;

    public static AbyssPlayerHistoryResponseDetailDto of(LolPlayerHistory lolPlayerHistory) {
        List<LolPlayer> players = lolPlayerHistory.getPlayers();
        List<AbyssPlayerDto> playerDtos = LolPlayer.toAbyssPlayerDtos(players);
        return new AbyssPlayerHistoryResponseDetailDto(
                lolPlayerHistory.getPlayerHistoryTitle(),
                playerDtos
        );
    }
}
