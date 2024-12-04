package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolTeam;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.AbyssResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.LolResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.RiftResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("abyssServcieImpl")
public class AbyssServiceImpl implements LolService {
    @Override
    public LolResponseDto createTeam(List<LolRequestDto> lolRequestDtos) {
        try {
            List<LolPlayer> players = lolRequestDtos.stream()
                    .map(LolRequestDto::toLolPlayer)
                    .collect(Collectors.toList());
            LolTeam one = splitTeam(players);
            LolTeam two = generateBalanceByTier(one);
            return AbyssResponseDto.of(two);
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
    }

    @Override
    public LolTeam splitTeam(List<LolPlayer> players) {
        List<LolPlayer> teamA = new ArrayList<>();
        List<LolPlayer> teamB = new ArrayList<>();
        try {
            Collections.shuffle(players);
            for ( int i=0; i<players.size()/2; i++ ){
                teamA.add(players.get(i));
                teamB.add(players.get(i + (players.size()/2)));
            }
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }

        return new LolTeam(teamA, teamB);
    }

    @Override
    public LolTeam generateBalanceByTier(LolTeam team) {
        List<LolPlayer> bestTeamA = new ArrayList<>();
        List<LolPlayer> bestTeamB = new ArrayList<>();
        try {
            List<LolPlayer> allPlayers = new ArrayList<>();
            allPlayers.addAll(team.getTeamA());
            allPlayers.addAll(team.getTeamB());
            // 모든 조합 탐색
            int n = allPlayers.size() / 2;
            List<List<LolPlayer>> allCombinations = generateCombinations(allPlayers, n);

            int minDifference = Integer.MAX_VALUE;


            // 각 조합에 대해 점수 차이를 계산
            for (List<LolPlayer> teamA : allCombinations) {
                List<LolPlayer> teamB = new ArrayList<>(allPlayers);
                teamB.removeAll(teamA);

                int teamA_score = calculateScore(teamA);
                int teamB_score = calculateScore(teamB);

                int difference = Math.abs(teamA_score - teamB_score);
                if (difference < minDifference) {
                    minDifference = difference;
                    bestTeamA = new ArrayList<>(teamA);
                    bestTeamB = new ArrayList<>(teamB);
                }
            }

        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        // 가장 밸런스가 좋은 팀 반환
        return new LolTeam(bestTeamA, bestTeamB);
    }

    private int calculateScore(List<LolPlayer> team) {
        return team.stream()
                .mapToInt(LolPlayer::getMmr)
                .sum();
    }

    private List<List<LolPlayer>> generateCombinations(List<LolPlayer> players, int size) {
        List<List<LolPlayer>> combinations = new ArrayList<>();
        generateCombinationsHelper(players, new ArrayList<>(), 0, size, combinations);
        return combinations;
    }

    private void generateCombinationsHelper(List<LolPlayer> players, List<LolPlayer> current, int index, int size, List<List<LolPlayer>> result) {
        if (current.size() == size) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (index == players.size()) return;

        // 현재 플레이어 포함
        current.add(players.get(index));
        generateCombinationsHelper(players, current, index + 1, size, result);

        // 현재 플레이어 제외
        current.remove(current.size() - 1);
        generateCombinationsHelper(players, current, index + 1, size, result);
    }
}
