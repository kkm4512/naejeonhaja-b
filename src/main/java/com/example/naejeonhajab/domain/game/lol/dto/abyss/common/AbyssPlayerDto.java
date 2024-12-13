package com.example.naejeonhajab.domain.game.lol.dto.abyss.common;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 클아이언트 요청으로 들어온 RiftRequestDto -> LolPlayer로 변경
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AbyssPlayerDto {
    @NotBlank(message = "이름은 공란 일 수 없습니다")
    String name;
    @NotNull(message = "티어는 공란 일 수 없습니다")
    LolTier tier;
    Integer mmr;

    public static List<AbyssPlayerDto> of(List<LolPlayerResult> playerResults) {
        return playerResults.stream()
                .map(dto -> new AbyssPlayerDto(dto.getName(), dto.getTier(), dto.getMmr()))
                .toList();
    }

    public void updateMmr(Integer score) {
        this.mmr = score;
    }
}

