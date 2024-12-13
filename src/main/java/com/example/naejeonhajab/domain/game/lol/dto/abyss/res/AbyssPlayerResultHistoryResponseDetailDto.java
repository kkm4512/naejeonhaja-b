package com.example.naejeonhajab.domain.game.lol.dto.abyss.res;

import com.example.naejeonhajab.domain.game.lol.dto.abyss.common.AbyssTeamResultDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftTeamResultDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.res.RiftPlayerResultHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 제목 + 10명의 유저 의 정보 전부있음
public class AbyssPlayerResultHistoryResponseDetailDto {
    private String playerResultHistoryTitle;
    @Valid
    private AbyssTeamResultDto teamA;
    @Valid
    private AbyssTeamResultDto teamB;

    public static AbyssPlayerResultHistoryResponseDetailDto of(LolPlayerResultHistory lolPlayerResultHistory) {
        String playerResultHistoryTitle = lolPlayerResultHistory.getPlayerResultHistoryTitle();
        AbyssTeamResultDto teamA = AbyssTeamResultDto.of(lolPlayerResultHistory.getPlayerResultOutcomes().get(0));
        AbyssTeamResultDto teamB = AbyssTeamResultDto.of(lolPlayerResultHistory.getPlayerResultOutcomes().get(1));
        return new AbyssPlayerResultHistoryResponseDetailDto(
                playerResultHistoryTitle,
                teamA,
                teamB
        );
    }
}
