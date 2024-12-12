package com.example.naejeonhajab.domain.game.lol.dto.rift.res;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftTeamResultDto;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 제목 + 10명의 유저 의 정보 전부있음
public class RiftPlayerResultHistoryResponseDetailDto {
    private String playerResultHistoryTitle;
    private RiftTeamResultDto teamA;
    private RiftTeamResultDto teamB;

    public static RiftPlayerResultHistoryResponseDetailDto of(LolPlayerResultHistory lolPlayerResultHistory){
        String playerResultHistoryTitle = lolPlayerResultHistory.getPlayerResultHistoryTitle();
        RiftTeamResultDto teamA = RiftTeamResultDto.of(lolPlayerResultHistory.getPlayerResultOutcomes().get(0));
        RiftTeamResultDto teamB = RiftTeamResultDto.of(lolPlayerResultHistory.getPlayerResultOutcomes().get(1));
        return new RiftPlayerResultHistoryResponseDetailDto(
            playerResultHistoryTitle,
                teamA,
                teamB
        );
    }
}
