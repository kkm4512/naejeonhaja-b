package com.example.naejeonhajab.domain.game.lol.dto.etc;

import com.example.naejeonhajab.domain.game.lol.enums.Line;
import com.example.naejeonhajab.domain.game.lol.enums.LineRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Lines {
    Line line;
    LineRole lineRole;
}
