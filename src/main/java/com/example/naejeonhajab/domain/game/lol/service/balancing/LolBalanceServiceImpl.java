package com.example.naejeonhajab.domain.game.lol.service.balancing;

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
    public LolTeamResponseDto generateBalanceByTier(LolTeamResponseDto dto) {
        List<LolPlayerDto> bestTeamA = new ArrayList<>();
        List<LolPlayerDto> bestTeamB = new ArrayList<>();
        try {
            // 모든 조합 탐색
            int n = dto.getTeamA().size();
            List<LolPlayerDto> allPlayers = new ArrayList<>(dto.getTeamA());
            allPlayers.addAll(dto.getTeamB());
            List<List<LolPlayerDto>> allCombinations = generateCombinations(allPlayers, n);
            int minDifference = Integer.MAX_VALUE;

            // 각 조합에 대해 점수 차이를 계산
            for (List<LolPlayerDto> teamA : allCombinations) {
                // 요청으로들어온 10명의 팀원과을 전부 teamB에 넣음
                List<LolPlayerDto> teamB = new ArrayList<>(allPlayers);
                // 현재 teamA에는 5명의 팀의 모든경우의수가 담겨져있기 떄문에, teamB에서 teamA를 제거해준후 5명을 남기면서 5:5 구도를 만든다
                teamB.removeAll(teamA);

                int teamA_score = lolUtilService.calculateScore(teamA);
                int teamB_score = lolUtilService.calculateScore(teamB);

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
        return new LolTeamResponseDto(bestTeamA, bestTeamB);
    }

    // 두 팀중 최적의 mmr점수 차이가 적은 조합을 찾는 메서드
    private List<List<LolPlayerDto>> generateCombinations(List<LolPlayerDto> players, int size) {
        List<List<LolPlayerDto>> combinations = new ArrayList<>();
        generateCombinationsHelper(players, new ArrayList<>(), 0, size, combinations);
        return combinations;
    }

    // 두 팀중 최적의 mmr점수 차이가 적은 조합을 찾는 메서드 실질적으로 이행하는 메서드
    private void generateCombinationsHelper(List<LolPlayerDto> players, List<LolPlayerDto> current, int index, int size, List<List<LolPlayerDto>> result) {
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
