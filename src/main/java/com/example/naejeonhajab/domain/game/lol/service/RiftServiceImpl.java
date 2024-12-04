package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolLines;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolTeam;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.LolResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.RiftResponseDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("riftServiceImpl")
public class RiftServiceImpl implements LolService {
    public LolResponseDto createTeam(List<LolRequestDto> lolRequestDtos) {
        int retries = 10000; // 최대 시도 횟수
        while (retries > 0) {
            try {
                List<LolPlayer> players = lolRequestDtos.stream()
                        .map(LolRequestDto::toLolPlayer)
                        .collect(Collectors.toList());
                LolTeam one = splitTeam(players);
                LolTeam two = generateBalanceByTier(one);
                LolTeam three = normalizeLineOrder(two);
                LolTeam four = checkLineAndRetryForLineBalance(three);
                LolTeam five = orderByLine(four);
                return RiftResponseDto.of(five);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }


    // 팀을 랜덤하게 5명으로 나눔
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

    // 티어를 기준으로 5:5 팀 나누기
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

    // MainLine으로 정렬된 새로운 팀을 반환
    private LolTeam normalizeLineOrder(LolTeam team) {
        List<LolPlayer> teamA = new ArrayList<>();
        List<LolPlayer> teamB = new ArrayList<>();
        try {
            teamA = soryTeamByLineRole(team.getTeamA());
            teamB = soryTeamByLineRole(team.getTeamB());
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return new LolTeam(teamA, teamB);
    }

    // MainLine과, 서브라인으로 구성하였음에도 라인이 채워지지 않는다면 5:5 팀을 구성하는것부터 다시 시도할 예정
    private LolTeam checkLineAndRetryForLineBalance(LolTeam team) {
        LolTeam newTeam = null;
        Map<LolLine, LolPlayer> teamALines = new HashMap<>();
        Map<LolLine, LolPlayer> teamBLines = new HashMap<>();
        try {
            // 1. 각 팀의 MainLine을 먼저 배정

            // 2. MainLine 배정 후에도 빈 라인이 존재한다면 SubLine으로 채움
            // 이게 True라면 A,B팀 둘중 한쪽이 채워지지 않았다는 뜻
            boolean hasEmptyLines = assignLines(team,teamALines,teamBLines);

            List<LolPlayer> teamA = updatePlayerLines(team.getTeamA(), teamALines);
            List<LolPlayer> teamB = updatePlayerLines(team.getTeamB(), teamBLines);
            newTeam = new LolTeam(teamA, teamB);

            if (hasEmptyLines) {
                throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
            }
            if (!hasEmptyLines) {
                return newTeam; // 처음 성공적으로 생성된 팀 즉시 반환
            }

        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return newTeam;
    }
//       라인순서대로 정리
    private LolTeam orderByLine(LolTeam team) {
        List<LolPlayer> sortedTeamA = new ArrayList<>();
        List<LolPlayer> sortedTeamB = new ArrayList<>();
        try {
            // 원하는 라인 순서
            List<LolLine> lineOrder = List.of(LolLine.values());

            // Team A 정렬
            sortedTeamA = sortTeamByLine(team.getTeamA(), lineOrder);

            // Team B 정렬
            sortedTeamB = sortTeamByLine(team.getTeamB(), lineOrder);

            // 정렬된 팀으로 새로운 Team 객체 반환
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return new LolTeam(sortedTeamA, sortedTeamB);
    }

    private List<LolPlayer> sortTeamByLine(List<LolPlayer> team, List<LolLine> lineOrder) {
        // Player를 정렬하는 로직
        return team.stream()
                .sorted(Comparator.comparingInt(player -> {
                    // 각 Player의 라인을 기준으로 정렬 우선순위를 설정
                    LolLine playerLine = player.getLines().get(0).getLine(); // 첫 번째 라인을 가져옴 (MainLine이 할당되었음)
                    return lineOrder.indexOf(playerLine); // 라인 순서의 인덱스 반환
                }))
                .collect(Collectors.toList());
    }



    // Player의 Line 정보를 Map에 맞게 업데이트하고, 새로운 List<Player>를 반환하는 메서드
    private List<LolPlayer> updatePlayerLines(List<LolPlayer> players, Map<LolLine, LolPlayer> lineMap) {
        return players.stream()
                .peek(player ->
                        lineMap.forEach((assignedLine, assignedPlayer) -> {
                            if (player.getName().equals(assignedPlayer.getName())) {
                                // true라면 SUbLINE 배정
                                if (player.isMmrReduced()) {
                                    player.updateLines(List.of(new LolLines(assignedLine, LolLineRole.SUBLINE)));
                                }
                                else {
                                    player.updateLines(List.of(new LolLines(assignedLine, LolLineRole.MAINLINE)));
                                }
                                // Player의 Lines를 업데이트
                            }
                        })
                )
                .collect(Collectors.toList());
    }



    private boolean assignLines(LolTeam team, Map<LolLine, LolPlayer> teamALines, Map<LolLine, LolPlayer> teamBLines) {

        assignTeamLines(team.getTeamA(), teamALines);
        assignTeamLines(team.getTeamB(), teamBLines);

        return hasEmptyLines(teamALines) || hasEmptyLines(teamBLines);
    }

    // TODO: 뭐가 문제지 여긴 잘 맞는거같은디
    private void assignTeamLines(List<LolPlayer> players, Map<LolLine, LolPlayer> lineMap) {
        for (LolPlayer player : players) {
            for (LolLines line : player.getLines()) {
                if (lineMap.containsKey(line.getLine())) continue;

                // MAINLINE 처리
                if (line.getLineRole() == LolLineRole.MAINLINE) {
                    lineMap.put(line.getLine(), player);
                    break; // 한 유저는 한 라인만 배정
                }

                // SUBLINE 처리
                if (line.getLineRole() == LolLineRole.SUBLINE) {
                    lineMap.put(line.getLine(), player);
                    player.subtractionMmr(200); // SubLine 배정 시 티어 점수 -200
                    player.updateMmrReduced();
                    break; // 한 유저는 한 라인만 배정
                }
            }
        }
    }


    // 존재하지않는 것이 하나라도 있으면 True 반환
    private boolean hasEmptyLines(Map<LolLine, LolPlayer> lineMap) {
        List<LolLine> requiredPositions = List.of(LolLine.values()); // Enum 값 그대로 사용
        return requiredPositions.stream().anyMatch(pos -> !lineMap.containsKey(pos));
    }



    // MainLine은 앞으로, SubLine은 뒤로
    private List<LolPlayer> soryTeamByLineRole(List<LolPlayer> players){
        return players.stream()
                .map(player -> {
                    // lines를 MainLine이 먼저 오도록 정렬
                    List<LolLines> reorderedLines = player.getLines().stream()
                            .sorted((line1, line2) -> {
                                if (line1.getLineRole().isMainRole()) return -1; // MainLine을 앞에 배치
                                if (line2.getLineRole().isMainRole()) return 1;
                                return 0; // 그 외는 순서를 유지
                            })
                            .toList();

                    // Player의 lines를 재배열 후 새로운 Player 객체 생성 (불변 객체일 경우)
                    return new LolPlayer(player.getName(), player.getTier(), reorderedLines, player.getMmr());
                })
                .collect(Collectors.toList());
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
