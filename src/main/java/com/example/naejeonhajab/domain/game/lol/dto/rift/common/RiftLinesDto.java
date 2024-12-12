package com.example.naejeonhajab.domain.game.lol.dto.rift.common;

import com.example.naejeonhajab.domain.game.lol.entity.result.LolResultLines;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftLinesDto {
    @NotNull(message = "라인은 공란 일 수 없습니다")
    LolLine line;
    @NotNull(message = "서브라인,메인라인은 공란 일 수 없습니다")
    LolLineRole lineRole;

    public static List<RiftLinesDto> of(List<LolResultLines> lines) {
        List<RiftLinesDto> result = new ArrayList<>();
        for (LolResultLines line : lines) {
            result.add(new RiftLinesDto(line.getLine(), line.getLineRole()));
        }
        return result;
    }
}
