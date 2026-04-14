package com.example.naejeonhaja.domain.game.lol.service.util;

import com.example.naejeonhaja.common.exception.BaseException;
import com.example.naejeonhaja.common.response.enums.BaseApiResponse;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LolUtilService {

    public LolTeamResponseDto splitTeam(List<LolPlayerDto> players) {
        try {
            Collections.shuffle(players);
            int half = players.size() / 2;
            return new LolTeamResponseDto(
                    new ArrayList<>(players.subList(0, half)),
                    new ArrayList<>(players.subList(half, players.size()))
            );
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
    }

    public int calculateScore(List<LolPlayerDto> team) {
        return team.stream().mapToInt(LolPlayerDto::getMmr).sum();
    }

    public void initMmr(List<LolPlayerDto> players) {
        players.forEach(p -> p.updateMmr(p.getTier().getScore()));
    }
}
