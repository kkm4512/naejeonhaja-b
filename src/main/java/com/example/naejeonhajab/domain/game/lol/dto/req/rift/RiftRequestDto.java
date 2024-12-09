package com.example.naejeonhajab.domain.game.lol.dto.req.rift;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RiftRequestDto implements LolRequestDto {
    String name;
    LolTier tier;
    List<LolLinesDto> lines;

    // 클라이언트로 부터 요청 데이터 -> 비즈니스 로직의 Player로 옮김
    @Override
    public LolPlayerDto toLolPlayer() {
        return new LolPlayerDto(name, tier, lines, tier.getScore());
    }
}
