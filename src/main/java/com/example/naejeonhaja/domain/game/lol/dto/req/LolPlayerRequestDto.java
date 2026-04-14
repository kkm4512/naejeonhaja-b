package com.example.naejeonhaja.domain.game.lol.dto.req;

import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerRequestDto {
    @Valid
    @Size(min = 10, max = 10, message = "정확히 10명의 플레이어가 필요합니다.")
    List<LolPlayerDto> lolPlayerDtos;
}
