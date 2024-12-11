package com.example.naejeonhajab.domain.game.lol.dto.res.rift.result;

import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// 제목 + id 데이터 전송
public class RiftPlayerResultHistoryResponseSimpleDto {
    private String playerResultHistoryTitle;
    private Long playerResultHistoryId;

    public static RiftPlayerResultHistoryResponseSimpleDto of(LolPlayerResultHistory playerResultHistory) {
        return new RiftPlayerResultHistoryResponseSimpleDto(
                playerResultHistory.getPlayerResultHistoryTitle(),
                playerResultHistory.getId()
        );
    }
}
