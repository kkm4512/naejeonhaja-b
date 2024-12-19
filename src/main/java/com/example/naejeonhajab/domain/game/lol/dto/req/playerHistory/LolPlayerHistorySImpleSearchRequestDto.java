package com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LolPlayerHistorySImpleSearchRequestDto {
    @Size(max = 50, message = "제목은 50자 를 초과할 수 없습니다")
    private String playerHistoryTitle;

    private int page;
}
