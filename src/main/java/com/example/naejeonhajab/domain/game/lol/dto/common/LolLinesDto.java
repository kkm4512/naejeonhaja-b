package com.example.naejeonhajab.domain.game.lol.dto.common;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolResultLines;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolLinesDto {
    LolLine line;
    LolLineRole lineRole;

    public static List<LolLinesDto> of(List<LolResultLines> lines) {
        List<LolLinesDto> result = new ArrayList<>();
        for (LolResultLines line : lines) {
            result.add(new LolLinesDto(line.getLine(), line.getLineRole()));
        }
        return result;
    }
}
