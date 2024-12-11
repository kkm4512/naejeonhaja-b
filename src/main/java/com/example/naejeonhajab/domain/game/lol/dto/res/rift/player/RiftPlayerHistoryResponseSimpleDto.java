package com.example.naejeonhajab.domain.game.lol.dto.res.rift.player;

import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// 제목 + id 데이터 전송
public class RiftPlayerHistoryResponseSimpleDto {
    private String playerHistoryTitle;
    private Long playerHistoryId;

    public static RiftPlayerHistoryResponseSimpleDto of(LolPlayerHistory playerHistory) {
        return new RiftPlayerHistoryResponseSimpleDto(
                playerHistory.getPlayerHistoryTitle(),
                playerHistory.getId()
        );
    }
}
