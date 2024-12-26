package com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// 제목 + id 데이터 전송
public class LolPlayerResultHistorySimpleDto {
    private String playerResultHistoryTitle;
    private Long playerResultHistoryId;

    public static LolPlayerResultHistorySimpleDto of(LolPlayerResultHistory playerResultHistory) {
        return new LolPlayerResultHistorySimpleDto(
                playerResultHistory.getPlayerResultHistoryTitle(),
                playerResultHistory.getId()
        );
    }

    public static List<LolPlayerResultHistorySimpleDto> of(List<LolPlayerResultHistory> playerHistorys) {
        return playerHistorys.stream().map(LolPlayerResultHistorySimpleDto::of).toList();
    }
}
