package com.example.naejeonhajab.domain.game.lol.service.balancing;

import com.example.naejeonhajab.annotation.TrackingTime;
import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.service.util.LolUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LolBalanceServiceImpl {
    private final LolUtilService lolUtilService;
    // 티어를 기준으로 5:5 팀 나누기
    // 일단 5:5일 모든 경우의 수를 구해야하네
    @TrackingTime
    public LolTeamResponseDto generateBalanceByTier(LolTeamResponseDto dto) {
        Integer compareInt = Integer.MAX_VALUE;
        List<LolPlayerDto> bestTeamA = new ArrayList<>();
        List<LolPlayerDto> bestTeamB = new ArrayList<>();
        List<LolPlayerDto> allPlayers = new ArrayList<>(dto.getTeamA());
        allPlayers.addAll(dto.getTeamB());

        int n = allPlayers.size();
        int teamSize = n / 2; // 5명씩 나누기

        // 1비트만큼 움직일때 2배가 되는 특성이 있음
        // 따라서 아래는 2^n 이됨
        // 0001 > 0010 > 0100 > 1000 ...
        for (int x = 0; x < (1 << n); x++) {
            List<LolPlayerDto> teamA = new ArrayList<>();
            List<LolPlayerDto> teamB = new ArrayList<>();

            for (int y = 0; y < n; y++) {
                if ((x & (1 << y)) != 0) {
                    teamA.add(allPlayers.get(y));
                } else {
                    teamB.add(allPlayers.get(y));
                }
            }

            // 팀 크기가 5명이 아니면 스킵
            if (teamA.size() != teamSize || teamB.size() != teamSize) {
                continue;
            }


            int teamAScore = lolUtilService.calculateScore(teamA);
            int teamBScore = lolUtilService.calculateScore(teamB);

            int difference = Math.abs(teamAScore - teamBScore);

            // 가지치기: 중간에 점수 차이가 이미 최소값보다 크다면 종료
            if (difference >= compareInt) {
                break;
            }

            // 최소 점수 차이를 업데이트
            compareInt = difference;
            bestTeamA = new ArrayList<>(teamA);
            bestTeamB = new ArrayList<>(teamB);
        }

        return new LolTeamResponseDto(bestTeamA, bestTeamB);
    }



}
