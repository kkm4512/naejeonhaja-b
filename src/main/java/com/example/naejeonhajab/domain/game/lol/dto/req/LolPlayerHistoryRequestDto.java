package com.example.naejeonhajab.domain.game.lol.dto.req;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerHistoryRequestDto {
    String playerHistoryTitle;
    @Valid
    @Size(max = 10, message = "최대 10명의 플레이어만 들어올 수 있습니다.")
    List<LolPlayerDto> lolPlayerDtos;
}