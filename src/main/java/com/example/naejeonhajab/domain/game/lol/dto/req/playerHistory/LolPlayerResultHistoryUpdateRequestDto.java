package com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerResultHistoryUpdateRequestDto {
    @Size(max = 50, message = "제목은 최대 50글자 까지 기재 가능합니다")
    String playerResultHistoryTitle;
}
