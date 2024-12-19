package com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory;

import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// 제목 + id 데이터 전송
public class LolPlayerHistoryResponseSimpleDto {
    private String playerHistoryTitle;
    private Long playerHistoryId;

    public static LolPlayerHistoryResponseSimpleDto of(LolPlayerHistory playerHistory) {
        return new LolPlayerHistoryResponseSimpleDto(
                playerHistory.getPlayerHistoryTitle(),
                playerHistory.getId()
        );
    }

    public static List<LolPlayerHistoryResponseSimpleDto> of(List<LolPlayerHistory> playerHistorys) {
        return playerHistorys.stream().map(LolPlayerHistoryResponseSimpleDto::of).toList();
    }
}
