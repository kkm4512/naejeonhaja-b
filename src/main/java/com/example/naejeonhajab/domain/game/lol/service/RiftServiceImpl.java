package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolTeamDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestPayloadDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.common.LolResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftResponseDto;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayerLines;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import com.example.naejeonhajab.domain.game.lol.repository.LolPlayerHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.repository.LolPlayerLinesRepository;
import com.example.naejeonhajab.domain.game.lol.repository.LolPlayerRepository;
import com.example.naejeonhajab.domain.user.entity.User;
import com.example.naejeonhajab.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_HISTORY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service("riftServiceImpl")
public class RiftServiceImpl implements LolService {
    private final LolPlayerHistoryRepository lolPlayerHistoryRepository;
    private final LolPlayerRepository lolPlayerRepository;
    private final LolPlayerLinesRepository lolPlayerLinesRepository;

    // 로그인한 사용자
    @Transactional
    public LolResponseDto createTeam(LolRequestPayloadDto lolRequestPayloadDto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory playerHistory = LolPlayerHistory.from(lolRequestPayloadDto,user);
        lolPlayerHistoryRepository.save(playerHistory);
        List<LolPlayer> playerList = LolPlayer.from(lolRequestPayloadDto,playerHistory);
        lolPlayerRepository.saveAll(playerList);
        List<LolPlayerLines> lines = LolPlayerLines.from(lolRequestPayloadDto,playerList);
        lolPlayerLinesRepository.saveAll(lines);

        int retries = 10000; // 최대 시도 횟수
        while (retries > 0) {
            try {
                List<LolPlayerDto> players = lolRequestPayloadDto.getRiftRequestDtos().stream()
                        .map(LolRequestDto::toLolPlayer)
                        .collect(Collectors.toList());
                LolTeamDto one = splitTeam(players);
                LolTeamDto two = generateBalanceByTier(one);
                LolTeamDto three = normalizeLineOrder(two);
                LolTeamDto four = checkLineAndRetryForLineBalance(three);
                LolTeamDto five = orderByLine(four);
                return RiftResponseDto.of(five);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }

    // 비로그인 사용자
    public LolResponseDto createTeam(LolRequestPayloadDto lolRequestPayloadDto) {
        int retries = 10000; // 최대 시도 횟수
        while (retries > 0) {
            try {
                List<LolPlayerDto> players = lolRequestPayloadDto.getRiftRequestDtos().stream()
                        .map(LolRequestDto::toLolPlayer)
                        .collect(Collectors.toList());
                LolTeamDto one = splitTeam(players);
                LolTeamDto two = generateBalanceByTier(one);
                LolTeamDto three = normalizeLineOrder(two);
                LolTeamDto four = checkLineAndRetryForLineBalance(three);
                LolTeamDto five = orderByLine(four);
                return RiftResponseDto.of(five);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }

    // 재생성
    public LolResponseDto createTeam(List<LolRequestDto> lolRequestDtos) {
        int retries = 10000; // 최대 시도 횟수
        while (retries > 0) {
            try {
                List<LolPlayerDto> players = lolRequestDtos.stream()
                        .map(LolRequestDto::toLolPlayer)
                        .collect(Collectors.toList());
                LolTeamDto one = splitTeam(players);
                LolTeamDto two = generateBalanceByTier(one);
                LolTeamDto three = normalizeLineOrder(two);
                LolTeamDto four = checkLineAndRetryForLineBalance(three);
                LolTeamDto five = orderByLine(four);
                return RiftResponseDto.of(five);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }


    // 팀을 랜덤하게 5명으로 나눔
    public LolTeamDto splitTeam(List<LolPlayerDto> players) {
        List<LolPlayerDto> teamA = new ArrayList<>();
        List<LolPlayerDto> teamB = new ArrayList<>();
        try {
            Collections.shuffle(players);
            for ( int i=0; i<players.size()/2; i++ ){
                teamA.add(players.get(i));
                teamB.add(players.get(i + (players.size()/2)));
            }
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }

        return new LolTeamDto(teamA, teamB);
    }

    // 티어를 기준으로 5:5 팀 나누기
    public LolTeamDto generateBalanceByTier(LolTeamDto team) {
        List<LolPlayerDto> bestTeamA = new ArrayList<>();
        List<LolPlayerDto> bestTeamB = new ArrayList<>();
        try {
            List<LolPlayerDto> allPlayers = new ArrayList<>();
            allPlayers.addAll(team.getTeamA());
            allPlayers.addAll(team.getTeamB());
            // 모든 조합 탐색
            int n = allPlayers.size() / 2;
            List<List<LolPlayerDto>> allCombinations = generateCombinations(allPlayers, n);

            int minDifference = Integer.MAX_VALUE;


            // 각 조합에 대해 점수 차이를 계산
            for (List<LolPlayerDto> teamA : allCombinations) {
                List<LolPlayerDto> teamB = new ArrayList<>(allPlayers);
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
        return new LolTeamDto(bestTeamA, bestTeamB);
    }

    // MainLine으로 정렬된 새로운 팀을 반환
    private LolTeamDto normalizeLineOrder(LolTeamDto team) {
        List<LolPlayerDto> teamA = new ArrayList<>();
        List<LolPlayerDto> teamB = new ArrayList<>();
        try {
            teamA = soryTeamByLineRole(team.getTeamA());
            teamB = soryTeamByLineRole(team.getTeamB());
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return new LolTeamDto(teamA, teamB);
    }

    // MainLine과, 서브라인으로 구성하였음에도 라인이 채워지지 않는다면 5:5 팀을 구성하는것부터 다시 시도할 예정
    private LolTeamDto checkLineAndRetryForLineBalance(LolTeamDto team) {
        LolTeamDto newTeam = null;
        Map<LolLine, LolPlayerDto> teamALines = new HashMap<>();
        Map<LolLine, LolPlayerDto> teamBLines = new HashMap<>();
        try {
            // 1. 각 팀의 MainLine을 먼저 배정

            // 2. MainLine 배정 후에도 빈 라인이 존재한다면 SubLine으로 채움
            // 이게 True라면 A,B팀 둘중 한쪽이 채워지지 않았다는 뜻
            boolean hasEmptyLines = assignLines(team,teamALines,teamBLines);

            List<LolPlayerDto> teamA = updatePlayerLines(team.getTeamA(), teamALines);
            List<LolPlayerDto> teamB = updatePlayerLines(team.getTeamB(), teamBLines);
            newTeam = new LolTeamDto(teamA, teamB);

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
    private LolTeamDto orderByLine(LolTeamDto team) {
        List<LolPlayerDto> sortedTeamA = new ArrayList<>();
        List<LolPlayerDto> sortedTeamB = new ArrayList<>();
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
        return new LolTeamDto(sortedTeamA, sortedTeamB);
    }

    private List<LolPlayerDto> sortTeamByLine(List<LolPlayerDto> team, List<LolLine> lineOrder) {
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
    private List<LolPlayerDto> updatePlayerLines(List<LolPlayerDto> players, Map<LolLine, LolPlayerDto> lineMap) {
        return players.stream()
                .peek(player ->
                        lineMap.forEach((assignedLine, assignedPlayer) -> {
                            if (player.getName().equals(assignedPlayer.getName())) {
                                // true라면 SUbLINE 배정
                                if (player.isMmrReduced()) {
                                    player.updateLines(List.of(new LolLinesDto(assignedLine, LolLineRole.SUBLINE)));
                                }
                                else {
                                    player.updateLines(List.of(new LolLinesDto(assignedLine, LolLineRole.MAINLINE)));
                                }
                                // Player의 Lines를 업데이트
                            }
                        })
                )
                .collect(Collectors.toList());
    }



    private boolean assignLines(LolTeamDto team, Map<LolLine, LolPlayerDto> teamALines, Map<LolLine, LolPlayerDto> teamBLines) {

        assignTeamLines(team.getTeamA(), teamALines);
        assignTeamLines(team.getTeamB(), teamBLines);

        return hasEmptyLines(teamALines) || hasEmptyLines(teamBLines);
    }

    private void assignTeamLines(List<LolPlayerDto> players, Map<LolLine, LolPlayerDto> lineMap) {
        for (LolPlayerDto player : players) {
            for (LolLinesDto line : player.getLines()) {
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
    private boolean hasEmptyLines(Map<LolLine, LolPlayerDto> lineMap) {
        List<LolLine> requiredPositions = List.of(LolLine.values()); // Enum 값 그대로 사용
        return requiredPositions.stream().anyMatch(pos -> !lineMap.containsKey(pos));
    }

    // MainLine은 앞으로, SubLine은 뒤로
    private List<LolPlayerDto> soryTeamByLineRole(List<LolPlayerDto> players){
        return players.stream()
                .map(player -> {
                    // lines를 MainLine이 먼저 오도록 정렬
                    List<LolLinesDto> reorderedLines = player.getLines().stream()
                            .sorted((line1, line2) -> {
                                if (line1.getLineRole().isMainRole()) return -1; // MainLine을 앞에 배치
                                if (line2.getLineRole().isMainRole()) return 1;
                                return 0; // 그 외는 순서를 유지
                            })
                            .toList();

                    // Player의 lines를 재배열 후 새로운 Player 객체 생성 (불변 객체일 경우)
                    return new LolPlayerDto(player.getName(), player.getTier(), reorderedLines, player.getMmr());
                })
                .collect(Collectors.toList());
    }



    private int calculateScore(List<LolPlayerDto> team) {
        return team.stream()
                .mapToInt(LolPlayerDto::getMmr)
                .sum();
    }

    private List<List<LolPlayerDto>> generateCombinations(List<LolPlayerDto> players, int size) {
        List<List<LolPlayerDto>> combinations = new ArrayList<>();
        generateCombinationsHelper(players, new ArrayList<>(), 0, size, combinations);
        return combinations;
    }

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

    @Override
    public RiftPlayerHistoryResponseDetailDto getDetailTeam(Long playerHistoryId) {
        // LolPlayerHistory를 Page로 가져온 후, map() 메서드를 사용하여 변환
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryByPlayerHistoryId(playerHistoryId);
        return RiftPlayerHistoryResponseDetailDto.of(lolPlayerHistory);
    }

    @Override
    public Page<RiftPlayerHistoryResponseSimpleDto> getSimpleTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerHistoryByUser(user, pageable)
                .map(RiftPlayerHistoryResponseSimpleDto::of);
    }

    private Page<LolPlayerHistory> findLolPlayerHistoryByUser(User user, Pageable pageable) {
        return lolPlayerHistoryRepository.findByUser(user, pageable);
    }

    private LolPlayerHistory findLolPlayerHistoryByPlayerHistoryId(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

}
