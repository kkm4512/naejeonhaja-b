package com.example.naejeonhajab.domain.game.lol.dto.req.playerResultHistory;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResultDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerResultHistoryRequestDto {
    @Size(max = 50, message = "제목은 최대 50글자 까지 기재 가능합니다")
    String playerResultHistoryTitle;
    @Valid
    LolTeamResultDto teamA;
    @Valid
    LolTeamResultDto teamB;
}
