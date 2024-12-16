package com.example.naejeonhajab.domain.game.lol.dto.res;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResultDto;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 제목 + 10명의 유저 의 정보 전부있음
public class LolPlayerResultHistoryResponseDetailDto {
    private String playerResultHistoryTitle;
    @Valid
    private LolTeamResultDto teamA;
    @Valid
    private LolTeamResultDto teamB;

    public static LolPlayerResultHistoryResponseDetailDto of(LolPlayerResultHistory lolPlayerResultHistory){
        String playerResultHistoryTitle = lolPlayerResultHistory.getPlayerResultHistoryTitle();
        LolTeamResultDto teamA = LolTeamResultDto.of(lolPlayerResultHistory.getPlayerResultOutcomes().get(0));
        LolTeamResultDto teamB = LolTeamResultDto.of(lolPlayerResultHistory.getPlayerResultOutcomes().get(1));
        return new LolPlayerResultHistoryResponseDetailDto(
            playerResultHistoryTitle,
                teamA,
                teamB
        );
    }
}
