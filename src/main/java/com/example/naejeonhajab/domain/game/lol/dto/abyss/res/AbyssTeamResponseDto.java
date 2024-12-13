package com.example.naejeonhajab.domain.game.lol.dto.abyss.res;

import com.example.naejeonhajab.domain.game.lol.dto.abyss.common.AbyssPlayerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 팀 분할
public class AbyssTeamResponseDto {
    @Valid
    @Size(max = 5, message = "최대 5명의 플레이어만 들어올 수 있습니다.")
    private List<AbyssPlayerDto> teamA;
    @Size(max = 10, message = "최대 5명의 플레이어만 들어올 수 있습니다.")
    private List<AbyssPlayerDto> teamB;

    public static AbyssTeamResponseDto of (List<AbyssPlayerDto> teamA, List<AbyssPlayerDto> teamB){
        return new AbyssTeamResponseDto(
                teamA,
                teamB
        );
    }
}
