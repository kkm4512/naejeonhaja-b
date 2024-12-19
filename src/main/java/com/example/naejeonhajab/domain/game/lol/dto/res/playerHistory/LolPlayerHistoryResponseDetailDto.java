package com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
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
public class LolPlayerHistoryResponseDetailDto {
    private String playerHistoryTitle;
    @Valid
    @Size
    private List<LolPlayerDto> playerDtos;

    public static LolPlayerHistoryResponseDetailDto of(LolPlayerHistory lolPlayerHistory){
        List<LolPlayer> players = lolPlayerHistory.getPlayers();
        List<LolPlayerDto> playerDtos = LolPlayer.to(players);
        return new LolPlayerHistoryResponseDetailDto(
                lolPlayerHistory.getPlayerHistoryTitle(),
                playerDtos
        );
    }
}
