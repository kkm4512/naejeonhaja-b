package com.example.naejeonhaja.domain.game.lol.dto.common;

import com.example.naejeonhaja.domain.game.lol.enums.LolLine;
import com.example.naejeonhaja.domain.game.lol.enums.LolLineRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LolLinesDto {
    LolLine line;
    LolLineRole lineRole;
}
