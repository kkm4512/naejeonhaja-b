package com.example.naejeonhajab.domain.game.lol.dto.res;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayer;

import java.util.List;

public interface LolResponseDto {
    List<LolPlayer> getTeamA();
    List<LolPlayer> getTeamB();
}
