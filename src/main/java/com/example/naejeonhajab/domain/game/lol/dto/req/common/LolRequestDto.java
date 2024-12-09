package com.example.naejeonhajab.domain.game.lol.dto.req.common;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;

public interface LolRequestDto {
    // 플레이어 이름
    String getName();
    LolTier getTier();
    LolPlayerDto toLolPlayer();
}
