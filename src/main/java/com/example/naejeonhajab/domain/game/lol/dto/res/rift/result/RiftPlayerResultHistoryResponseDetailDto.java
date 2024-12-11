package com.example.naejeonhajab.domain.game.lol.dto.res.rift.result;

import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.enums.LolTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 제목 + 10명의 유저 의 정보 전부있음
public class RiftPlayerResultHistoryResponseDetailDto {
    private String playerResultHistoryTitle;
    private List<RiftPlayerResultResultResponseDto> teamA;
    private List<RiftPlayerResultResultResponseDto> teamB;

    public static RiftPlayerResultHistoryResponseDetailDto of(LolPlayerResultHistory lolPlayerResultHistory){
        String playerResultHistoryTitle = lolPlayerResultHistory.getPlayerResultHistoryTitle();
        List<LolPlayerResult> teamAEntity = lolPlayerResultHistory.getPlayerResults().stream().filter(i -> i.getTeam().equals(LolTeam.TEAM_A)).collect(Collectors.toList());
        List<LolPlayerResult> teamBEntity = lolPlayerResultHistory.getPlayerResults().stream().filter(i -> i.getTeam().equals(LolTeam.TEAM_B)).collect(Collectors.toList());
        List<RiftPlayerResultResultResponseDto> teamA = LolPlayerResult.of(teamAEntity);
        List<RiftPlayerResultResultResponseDto> teamB = LolPlayerResult.of(teamBEntity);
        return new RiftPlayerResultHistoryResponseDetailDto(
            playerResultHistoryTitle,
                teamA,
                teamB
        );
    }
}
