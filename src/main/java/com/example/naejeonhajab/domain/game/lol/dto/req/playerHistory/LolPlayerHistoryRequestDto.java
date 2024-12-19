package com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory;

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
    @Size(max = 50, message = "제목은 최대 50글자 까지 기재 가능합니다")
    String playerHistoryTitle;
    @Valid
    @Size(max = 10, message = "최대 10명의 플레이어만 들어올 수 있습니다.")
    List<LolPlayerDto> lolPlayerDtos;
}
