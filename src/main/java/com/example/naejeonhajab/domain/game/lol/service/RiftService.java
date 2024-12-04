package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.etc.Lines;
import com.example.naejeonhajab.domain.game.lol.dto.etc.Player;
import com.example.naejeonhajab.domain.game.lol.dto.etc.Team;
import com.example.naejeonhajab.domain.game.lol.dto.req.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.RiftResponseDto;
import com.example.naejeonhajab.domain.game.lol.enums.Line;
import com.example.naejeonhajab.domain.game.lol.enums.LineRole;
import com.example.naejeonhajab.domain.game.lol.enums.Tier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RiftService {
    public RiftResponseDto createTeam(List<RiftRequestDto> riftRequestDtos) {
        int retries = 10000; // 최대 시도 횟수
        while (retries > 0) {
            try {
                List<Player> players = RiftRequestDto.of(riftRequestDtos);
                Team one = splitTeam(players);
                Team two = generateBalanceByTier(one);
                Team three = normalizeLineOrder(two);
                Team four = checkLineAndRetryForLineBalance(three);
                Team five = orderByLine(four);
                return RiftResponseDto.of(five);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }


    // 팀을 랜덤하게 5명으로 나눔
    private Team splitTeam(List<Player> players) {
        List<Player> teamA = new ArrayList<>();
        List<Player> teamB = new ArrayList<>();
        try {
            Collections.shuffle(players);
            for ( int i=0; i<players.size()/2; i++ ){
                teamA.add(players.get(i));
                teamB.add(players.get(i+5));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return new Team(teamA, teamB);
    }

    // 티어를 기준으로 5:5 팀 나누기
    private Team generateBalanceByTier(Team team) {
        List<Player> bestTeamA = new ArrayList<>();
        List<Player> bestTeamB = new ArrayList<>();
        try {
            List<Player> allPlayers = new ArrayList<>();
            allPlayers.addAll(team.getTeamA());
            allPlayers.addAll(team.getTeamB());
            // 모든 조합 탐색
            int n = allPlayers.size() / 2;
            List<List<Player>> allCombinations = generateCombinations(allPlayers, n);

            int minDifference = Integer.MAX_VALUE;


            // 각 조합에 대해 점수 차이를 계산
            for (List<Player> teamA : allCombinations) {
                List<Player> teamB = new ArrayList<>(allPlayers);
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
            log.error(e.getMessage());
        }
        // 가장 밸런스가 좋은 팀 반환
        return new Team(bestTeamA, bestTeamB);
    }

    // MainLine으로 정렬된 새로운 팀을 반환
    private Team normalizeLineOrder(Team team) {
        List<Player> teamA = new ArrayList<>();
        List<Player> teamB = new ArrayList<>();
        try {
            teamA = soryTeamByLineRole(team.getTeamA());
            teamB = soryTeamByLineRole(team.getTeamB());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Team(teamA, teamB);
    }

    // MainLine과, 서브라인으로 구성하였음에도 라인이 채워지지 않는다면 5:5 팀을 구성하는것부터 다시 시도할 예정
    // TODO: 라인 중복 문제 해결 안됨.
    private Team checkLineAndRetryForLineBalance(Team team) {
        Team newTeam = null;
        Map<Line, Player> teamALines = new HashMap<>();
        Map<Line, Player> teamBLines = new HashMap<>();
        try {
            // 1. 각 팀의 MainLine을 먼저 배정

            // 2. MainLine 배정 후에도 빈 라인이 존재한다면 SubLine으로 채움
            // 이게 True라면 A,B팀 둘중 한쪽이 채워지지 않았다는 뜻
            boolean hasEmptyLines = assignLines(team,teamALines,teamBLines);

            List<Player> teamA = updatePlayerLines(team.getTeamA(), teamALines);
            List<Player> teamB = updatePlayerLines(team.getTeamB(), teamBLines);
            newTeam = new Team(teamA, teamB);

            if (hasEmptyLines) {
                throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
            }
            if (!hasEmptyLines) {
                return newTeam; // 처음 성공적으로 생성된 팀 즉시 반환
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return newTeam;
    }
//       라인순서대로 정리
    private Team orderByLine(Team team) {
        List<Player> sortedTeamA = new ArrayList<>();
        List<Player> sortedTeamB = new ArrayList<>();
        try {
            // 원하는 라인 순서
            List<Line> lineOrder = List.of(Line.values());

            // Team A 정렬
            sortedTeamA = sortTeamByLine(team.getTeamA(), lineOrder);

            // Team B 정렬
            sortedTeamB = sortTeamByLine(team.getTeamB(), lineOrder);

            // 정렬된 팀으로 새로운 Team 객체 반환
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Team(sortedTeamA, sortedTeamB);

    }

    private List<Player> sortTeamByLine(List<Player> team, List<Line> lineOrder) {
        // Player를 정렬하는 로직
        return team.stream()
                .sorted(Comparator.comparingInt(player -> {
                    // 각 Player의 라인을 기준으로 정렬 우선순위를 설정
                    Line playerLine = player.getLines().get(0).getLine(); // 첫 번째 라인을 가져옴 (MainLine이 할당되었음)
                    return lineOrder.indexOf(playerLine); // 라인 순서의 인덱스 반환
                }))
                .collect(Collectors.toList());
    }



    // Player의 Line 정보를 Map에 맞게 업데이트하고, 새로운 List<Player>를 반환하는 메서드
    // Player의 Line 정보를 Map에 맞게 업데이트하고, 새로운 List<Player>를 반환하는 메서드
    private List<Player> updatePlayerLines(List<Player> players, Map<Line, Player> lineMap) {
        return players.stream()
                .peek(player ->
                        lineMap.forEach((assignedLine, assignedPlayer) -> {
                            if (player.getName().equals(assignedPlayer.getName())) {
                                // Player의 Lines를 업데이트
                                player.updateLines(List.of(new Lines(assignedLine, LineRole.MAINLINE)));
                            }
                        })
                )
                .collect(Collectors.toList());
    }



    private boolean assignLines(Team team, Map<Line, Player> teamALines, Map<Line, Player> teamBLines) {

        assignTeamLines(team.getTeamA(), teamALines);
        assignTeamLines(team.getTeamB(), teamBLines);

        return hasEmptyLines(teamALines) || hasEmptyLines(teamBLines);
    }

    // TODO: 뭐가 문제지 여긴 잘 맞는거같은디
    private void assignTeamLines(List<Player> players, Map<Line, Player> lineMap) {
        for (Player player : players) {
            for (Lines line : player.getLines()) {
                if (lineMap.containsKey(line.getLine())) continue;

                // MAINLINE 처리
                if (line.getLineRole() == LineRole.MAINLINE) {
                    lineMap.put(line.getLine(), player);
                    break; // 한 유저는 한 라인만 배정
                }

                // SUBLINE 처리
                if (line.getLineRole() == LineRole.SUBLINE) {
                    lineMap.put(line.getLine(), player);
                    player.subtractionMmr(200); // SubLine 배정 시 티어 점수 -200
                    player.updateMmrReduced();
                    break; // 한 유저는 한 라인만 배정
                }
            }
        }
    }


    // 존재하지않는 것이 하나라도 있으면 True 반환
    private boolean hasEmptyLines(Map<Line, Player> lineMap) {
        List<Line> requiredPositions = List.of(Line.values()); // Enum 값 그대로 사용
        return requiredPositions.stream().anyMatch(pos -> !lineMap.containsKey(pos));
    }



    // MainLine은 앞으로, SubLine은 뒤로
    private List<Player> soryTeamByLineRole(List<Player> players){
        return players.stream()
                .map(player -> {
                    // lines를 MainLine이 먼저 오도록 정렬
                    List<Lines> reorderedLines = player.getLines().stream()
                            .sorted((line1, line2) -> {
                                if (line1.getLineRole().isMainRole()) return -1; // MainLine을 앞에 배치
                                if (line2.getLineRole().isMainRole()) return 1;
                                return 0; // 그 외는 순서를 유지
                            })
                            .toList();

                    // Player의 lines를 재배열 후 새로운 Player 객체 생성 (불변 객체일 경우)
                    return new Player(player.getName(), player.getTier(), reorderedLines, player.getMmr());
                })
                .collect(Collectors.toList());
    }



    private int calculateScore(List<Player> team) {
        return team.stream()
                .mapToInt(Player::getMmr)
                .sum();
    }

    private List<List<Player>> generateCombinations(List<Player> players, int size) {
        List<List<Player>> combinations = new ArrayList<>();
        generateCombinationsHelper(players, new ArrayList<>(), 0, size, combinations);
        return combinations;
    }

    private void generateCombinationsHelper(List<Player> players, List<Player> current, int index, int size, List<List<Player>> result) {
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
