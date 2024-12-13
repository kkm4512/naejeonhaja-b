package com.example.naejeonhajab.domain.game.lol.dto.rift.common;

import com.example.naejeonhajab.domain.game.lol.entity.player.LolLines;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftPlayerHistoryAndTeamDto {
    private Long userId; // User의 ID
    private String playerHistoryTitle; // LolPlayerHistory의 제목
    private LolType type; // LolType (ENUM 값을 String으로 저장)
    private List<LolPlayer> playerList; // 플레이어 리스트
    private List<LolLines> lineList; // 라인 리스트
}
