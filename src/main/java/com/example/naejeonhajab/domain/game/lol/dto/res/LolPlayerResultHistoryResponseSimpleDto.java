package com.example.naejeonhajab.domain.game.lol.dto.res;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// 제목 + id 데이터 전송
public class LolPlayerResultHistoryResponseSimpleDto {
    private String playerResultHistoryTitle;
    private Long playerResultHistoryId;

    public static LolPlayerResultHistoryResponseSimpleDto of(LolPlayerResultHistory playerResultHistory) {
        return new LolPlayerResultHistoryResponseSimpleDto(
                playerResultHistory.getPlayerResultHistoryTitle(),
                playerResultHistory.getId()
        );
    }
}
