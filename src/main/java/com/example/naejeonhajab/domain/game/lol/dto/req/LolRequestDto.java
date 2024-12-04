package com.example.naejeonhajab.domain.game.lol.dto.req;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;

public interface LolRequestDto {
    // 플레이어 이름
    String getName();
    LolTier getTier();
    LolPlayer toLolPlayer();
}
