package com.example.naejeonhajab.domain.game.lol.dto.abyss.common;

import com.example.naejeonhajab.common.enums.Outcome;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftTeamResultDto;
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
public class AbyssTeamResultDto {
    @NotNull(message = "승리 여부는 공백 일 수 없습니다")
    Outcome outcome;
    @Valid
    @Size(max = 10, message = "최대 10명의 플레이어만 들어올 수 있습니다.")
    List<AbyssPlayerDto> team;

    public static AbyssTeamResultDto of(LolPlayerResultOutcome lolPlayerResultOutcome) {
        return new AbyssTeamResultDto(
                lolPlayerResultOutcome.getOutcome(),
                AbyssPlayerDto.of(lolPlayerResultOutcome.getPlayerResults())
        );
    }
}
