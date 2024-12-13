package com.example.naejeonhajab.domain.game.lol.dto.req;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResultDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerResultHistoryRequestDto {
    @NotBlank(message = "대전결과 제목은 공백 일 수 없습니다")
    String playerResultHistoryTitle;
    @Valid
    LolTeamResultDto teamA;
    @Valid
    LolTeamResultDto teamB;
}
