package com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory;

import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistorySimpleDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolPlayerHistoryDeleteAllRequestDto {
    @Valid
    @Size(max = 3, message = "최대 3개의 플레이어 내역을 삭제 할 수 있습니다")
    List<LolPlayerHistorySimpleDto> lolPlayerHistorySimpleDtos;
}
