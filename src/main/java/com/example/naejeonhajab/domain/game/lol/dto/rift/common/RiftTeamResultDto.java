package com.example.naejeonhajab.domain.game.lol.dto.rift.common;

import com.example.naejeonhajab.common.enums.Outcome;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultOutcome;
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
public class RiftTeamResultDto {
    @NotNull(message = "승리 여부는 공백 일 수 없습니다")
    Outcome outcome;
    @Valid
    @Size(max = 5, message = "팀은 5명이 최대 인원입니다")
    List<RiftPlayerDto> team;

    public static RiftTeamResultDto of(LolPlayerResultOutcome lolPlayerResultOutcome) {
        return new RiftTeamResultDto(
                lolPlayerResultOutcome.getOutcome(),
                RiftPlayerDto.of(lolPlayerResultOutcome.getPlayerResults())
        );
    }
}
