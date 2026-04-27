package com.example.naejeonhaja.domain.game.lol.service.util;

import com.example.naejeonhaja.common.exception.BaseException;
import com.example.naejeonhaja.common.response.enums.BaseApiResponse;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.enums.LolBalanceEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LolUtilService {

    public LolTeamResponseDto splitTeam(List<LolPlayerDto> players) {
        try {
            // MMR 내림차순 정렬
            players.sort((a, b) -> Integer.compare(b.getMmr(), a.getMmr()));
            List<LolPlayerDto> teamA = new ArrayList<>();
            List<LolPlayerDto> teamB = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (i % 2 == 0) {
                    teamA.add(players.get(i));
                } else {
                    teamB.add(players.get(i));
                }
            }
            LolBalanceEnum balance = calculateBalance(teamA, teamB);
            return LolTeamResponseDto.of(teamA, teamB, balance);
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
    }

    public int calculateScore(List<LolPlayerDto> team) {
        return team.stream().mapToInt(LolPlayerDto::getMmr).sum();
    }

    public void initMmr(List<LolPlayerDto> players) {
        players.forEach(p -> {
            if (p.getMmr() == null || p.getMmr() == 0) {
                p.updateMmr(p.getTier().getScore());
            }
        });
    }

    public LolBalanceEnum calculateBalance(List<LolPlayerDto> teamA, List<LolPlayerDto> teamB) {
        int sumA = teamA.stream().mapToInt(LolPlayerDto::getMmr).sum();
        int sumB = teamB.stream().mapToInt(LolPlayerDto::getMmr).sum();
        int diff = Math.abs(sumA - sumB);
        if (diff <= 100) return LolBalanceEnum.GOOD;
        else if (diff <= 300) return LolBalanceEnum.FAIR;
        else return LolBalanceEnum.BAD;
    }
}
