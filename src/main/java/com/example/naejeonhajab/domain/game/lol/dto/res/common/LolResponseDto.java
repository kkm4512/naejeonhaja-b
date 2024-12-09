package com.example.naejeonhajab.domain.game.lol.dto.res.common;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;

import java.util.List;

public interface LolResponseDto {
    List<LolPlayerDto> getTeamA();
    List<LolPlayerDto> getTeamB();
}
