package com.example.naejeonhajab.domain.game.lol.dto.req.rift;

import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftLinesRequestDto {
    LolLine line;
    LolLineRole lineRole;
}
