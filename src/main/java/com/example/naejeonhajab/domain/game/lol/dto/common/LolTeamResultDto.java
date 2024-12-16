package com.example.naejeonhajab.domain.game.lol.dto.common;

import com.example.naejeonhajab.common.enums.Outcome;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultOutcome;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolTeamResultDto {
    @NotNull(message = "승리 여부는 공백 일 수 없습니다")
    Outcome outcome;
    @Valid
    @Size(max = 5, message = "팀은 5명이 최대 인원입니다")
    List<LolPlayerDto> team;

    public static LolTeamResultDto of(LolPlayerResultOutcome lolPlayerResultOutcome) {
        return new LolTeamResultDto(
                lolPlayerResultOutcome.getOutcome(),
                LolPlayerDto.of(lolPlayerResultOutcome.getPlayerResults())
        );
    }
}
