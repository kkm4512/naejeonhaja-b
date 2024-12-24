package com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory;

import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// 제목 + id 데이터 전송
public class LolPlayerHistorySimpleDto {
    @Size(max = 50, message = "제목은 최대 50글자 까지 기재 가능합니다")
    private String playerHistoryTitle;
    @NotNull(message = "플레이어 히스토리 내역 Id는 공백 일 수 없습니다")
    private Long playerHistoryId;

    public static LolPlayerHistorySimpleDto of(LolPlayerHistory playerHistory) {
        return new LolPlayerHistorySimpleDto(
                playerHistory.getPlayerHistoryTitle(),
                playerHistory.getId()
        );
    }

    public static List<LolPlayerHistorySimpleDto> of(List<LolPlayerHistory> playerHistorys) {
        return playerHistorys.stream().map(LolPlayerHistorySimpleDto::of).toList();
    }
}
