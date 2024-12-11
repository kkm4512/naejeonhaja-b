package com.example.naejeonhajab.domain.game.lol.dto.req.rift.player;

import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftLinesRequestDto {
    @NotNull(message = "라인은 공란 일 수 없습니다")
    LolLine line;
    @NotNull(message = "서브라인,메인라인은 공란 일 수 없습니다")
    LolLineRole lineRole;
}
