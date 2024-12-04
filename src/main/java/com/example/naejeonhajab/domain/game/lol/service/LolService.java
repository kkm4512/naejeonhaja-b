package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolTeam;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.LolResponseDto;

import java.util.List;

public interface LolService {
    LolResponseDto createTeam(List<LolRequestDto> lolRequestDtos);
    LolTeam splitTeam(List<LolPlayer> players);
    LolTeam generateBalanceByTier(LolTeam team);
}
