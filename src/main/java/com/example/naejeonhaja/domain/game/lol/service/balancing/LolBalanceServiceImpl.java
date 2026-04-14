package com.example.naejeonhaja.domain.game.lol.service.balancing;

import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.service.util.LolUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LolBalanceServiceImpl {

    private final LolUtilService lolUtilService;

    public LolTeamResponseDto generateBalanceByTier(LolTeamResponseDto dto) {
        int compareInt = Integer.MAX_VALUE;
        List<LolPlayerDto> bestTeamA = new ArrayList<>();
        List<LolPlayerDto> bestTeamB = new ArrayList<>();
        List<LolPlayerDto> all = new ArrayList<>(dto.getTeamA());
        all.addAll(dto.getTeamB());

        int n = all.size();
        int teamSize = n / 2;

        for (int x = 0; x < (1 << n); x++) {
            List<LolPlayerDto> teamA = new ArrayList<>();
            List<LolPlayerDto> teamB = new ArrayList<>();
            for (int y = 0; y < n; y++) {
                if ((x & (1 << y)) != 0) teamA.add(all.get(y));
                else teamB.add(all.get(y));
            }
            if (teamA.size() != teamSize || teamB.size() != teamSize) continue;

            int diff = Math.abs(lolUtilService.calculateScore(teamA) - lolUtilService.calculateScore(teamB));
            if (diff < compareInt) {
                compareInt = diff;
                bestTeamA = new ArrayList<>(teamA);
                bestTeamB = new ArrayList<>(teamB);
            }
        }
        return new LolTeamResponseDto(bestTeamA, bestTeamB);
    }
}
