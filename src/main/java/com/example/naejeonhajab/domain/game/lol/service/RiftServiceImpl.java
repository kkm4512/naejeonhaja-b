package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftLinesRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.result.RiftPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.player.RiftPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.player.RiftPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.player.RiftTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.result.RiftPlayerResultHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.result.RiftPlayerResultHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolLines;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolResultLines;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import com.example.naejeonhajab.domain.game.lol.enums.LolTeam;
import com.example.naejeonhajab.domain.game.lol.repository.*;
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
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_RESULT_HISTORY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service("riftServiceImpl")
public class RiftServiceImpl {
    private final LolPlayerHistoryRepository lolPlayerHistoryRepository;
    private final LolPlayerRepository lolPlayerRepository;
    private final LolLinesRepository lolLinesRepository;

    private final LolPlayerResultHistoryRepository lolPlayerResultHistoryRepository;
    private final LolPlayerResultRepository lolPlayerResultRepository;
    private final LolResultLinesRepository lolResultLinesRepository;


    // 로그인한 사용자 && 팀생성 && 히스토리 저장
    @Transactional
    public RiftTeamResponseDto createPlayerHistoryAndTeam(RiftPlayerHistoryRequestDto lolRequestPayloadDto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory playerHistory = LolPlayerHistory.from(lolRequestPayloadDto,user);
        lolPlayerHistoryRepository.save(playerHistory);
        List<LolPlayer> playerList = LolPlayer.from(lolRequestPayloadDto,playerHistory);
        lolPlayerRepository.saveAll(playerList);
        List<LolLines> lines = LolLines.from(lolRequestPayloadDto,playerList);
        lolLinesRepository.saveAll(lines);
        return create(lolRequestPayloadDto.getRiftPlayerRequestDtos());
    }

    // 재생성
    public RiftTeamResponseDto  createTeam(List<RiftPlayerRequestDto> riftPlayerRequestDtos) {
       return create(riftPlayerRequestDtos);
    }

    // 내전 결과 저장 메서드
    @Transactional
    public void createResultTeam(RiftPlayerResultHistoryRequestDto riftPlayerResultHistoryRequestDto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerResultHistory playerResultHistory = LolPlayerResultHistory.from(riftPlayerResultHistoryRequestDto,user);
        lolPlayerResultHistoryRepository.save(playerResultHistory);
        List<LolPlayerResult> playerResultsA = LolPlayerResult.from(riftPlayerResultHistoryRequestDto,playerResultHistory, LolTeam.TEAM_A);
        List<LolPlayerResult> playerResultsB = LolPlayerResult.from(riftPlayerResultHistoryRequestDto,playerResultHistory, LolTeam.TEAM_B);
        lolPlayerResultRepository.saveAll(playerResultsA);
        lolPlayerResultRepository.saveAll(playerResultsB);
        List<LolResultLines> linesA = LolResultLines.from(riftPlayerResultHistoryRequestDto,playerResultsA,LolTeam.TEAM_A);
        List<LolResultLines> linesB = LolResultLines.from(riftPlayerResultHistoryRequestDto,playerResultsB,LolTeam.TEAM_B);
        lolResultLinesRepository.saveAll(linesA);
        lolResultLinesRepository.saveAll(linesB);
    }

    // 팀을 랜덤하게 5명으로 나눔
    public RiftTeamResponseDto splitTeam(List<RiftPlayerRequestDto> riftPlayerRequestDtos) {
        List<RiftPlayerRequestDto> teamA = new ArrayList<>();
        List<RiftPlayerRequestDto> teamB = new ArrayList<>();
        try {
            Collections.shuffle(riftPlayerRequestDtos);
            for ( int i=0; i<riftPlayerRequestDtos.size()/2; i++ ){
                teamA.add(riftPlayerRequestDtos.get(i));
                teamB.add(riftPlayerRequestDtos.get(i + (riftPlayerRequestDtos.size()/2)));
            }
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }

        return new RiftTeamResponseDto(teamA, teamB);
    }

    // 티어를 기준으로 5:5 팀 나누기
    public RiftTeamResponseDto generateBalanceByTier(RiftTeamResponseDto team) {
        List<RiftPlayerRequestDto> bestTeamA = new ArrayList<>();
        List<RiftPlayerRequestDto> bestTeamB = new ArrayList<>();
        try {
            List<RiftPlayerRequestDto> allPlayers = new ArrayList<>();
            allPlayers.addAll(team.getTeamA());
            allPlayers.addAll(team.getTeamB());
            // 모든 조합 탐색
            int n = allPlayers.size() / 2;
            List<List<RiftPlayerRequestDto>> allCombinations = generateCombinations(allPlayers, n);

            int minDifference = Integer.MAX_VALUE;


            // 각 조합에 대해 점수 차이를 계산
            for (List<RiftPlayerRequestDto> teamA : allCombinations) {
                List<RiftPlayerRequestDto> teamB = new ArrayList<>(allPlayers);
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
        return new RiftTeamResponseDto(bestTeamA, bestTeamB);
    }

    // MainLine으로 정렬된 새로운 팀을 반환
    private RiftTeamResponseDto normalizeLineOrder(RiftTeamResponseDto team) {
        List<RiftPlayerRequestDto> teamA;
        List<RiftPlayerRequestDto> teamB;
        try {
            teamA = soryTeamByLineRole(team.getTeamA());
            teamB = soryTeamByLineRole(team.getTeamB());
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return new RiftTeamResponseDto(teamA, teamB);
    }

    // MainLine과, 서브라인으로 구성하였음에도 라인이 채워지지 않는다면 5:5 팀을 구성하는것부터 다시 시도할 예정
    private RiftTeamResponseDto checkLineAndRetryForLineBalance(RiftTeamResponseDto team) {
        RiftTeamResponseDto newTeam;
        Map<LolLine, RiftPlayerRequestDto> teamALines = new HashMap<>();
        Map<LolLine, RiftPlayerRequestDto> teamBLines = new HashMap<>();
        try {
            // 1. 각 팀의 MainLine을 먼저 배정

            // 2. MainLine 배정 후에도 빈 라인이 존재한다면 SubLine으로 채움
            // 이게 True라면 A,B팀 둘중 한쪽이 채워지지 않았다는 뜻
            boolean hasEmptyLines = assignLines(team,teamALines,teamBLines);

            List<RiftPlayerRequestDto> teamA = updatePlayerLines(team.getTeamA(), teamALines);
            List<RiftPlayerRequestDto> teamB = updatePlayerLines(team.getTeamB(), teamBLines);
            newTeam = new RiftTeamResponseDto(teamA, teamB);

            if (hasEmptyLines) {
                throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
            }
            else {
                return newTeam; // 처음 성공적으로 생성된 팀 즉시 반환
            }

        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
    }
//       라인순서대로 정리
    private RiftTeamResponseDto orderByLine(RiftTeamResponseDto team) {
        List<RiftPlayerRequestDto> sortedTeamA;
        List<RiftPlayerRequestDto> sortedTeamB;
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
        return new RiftTeamResponseDto(sortedTeamA, sortedTeamB);
    }

    private List<RiftPlayerRequestDto> sortTeamByLine(List<RiftPlayerRequestDto> riftPlayerRequestDtos, List<LolLine> lineOrder) {
        // Player를 정렬하는 로직
        return riftPlayerRequestDtos.stream()
                .sorted(Comparator.comparingInt(player -> {
                    // 각 Player의 라인을 기준으로 정렬 우선순위를 설정
                    LolLine playerLine = player.getLines().get(0).getLine(); // 첫 번째 라인을 가져옴 (MainLine이 할당되었음)
                    return lineOrder.indexOf(playerLine); // 라인 순서의 인덱스 반환
                }))
                .collect(Collectors.toList());
    }



    // Player의 Line 정보를 Map에 맞게 업데이트하고, 새로운 List<Player>를 반환하는 메서드
    private List<RiftPlayerRequestDto> updatePlayerLines(List<RiftPlayerRequestDto> riftPlayerRequestDtos, Map<LolLine, RiftPlayerRequestDto> lineMap) {
        return riftPlayerRequestDtos.stream()
                .peek(player ->
                        lineMap.forEach((assignedLine, assignedPlayer) -> {
                            if (player.getName().equals(assignedPlayer.getName())) {
                                // true라면 SUbLINE 배정
                                if (player.isMmrReduced()) {
                                    player.updateLines(List.of(new RiftLinesRequestDto(assignedLine, LolLineRole.SUBLINE)));
                                }
                                else {
                                    player.updateLines(List.of(new RiftLinesRequestDto(assignedLine, LolLineRole.MAINLINE)));
                                }
                                // Player의 Lines를 업데이트
                            }
                        })
                )
                .collect(Collectors.toList());
    }



    private boolean assignLines(RiftTeamResponseDto team, Map<LolLine, RiftPlayerRequestDto> teamALines, Map<LolLine, RiftPlayerRequestDto> teamBLines) {

        assignTeamLines(team.getTeamA(), teamALines);
        assignTeamLines(team.getTeamB(), teamBLines);

        return hasEmptyLines(teamALines) || hasEmptyLines(teamBLines);
    }

    private void assignTeamLines(List<RiftPlayerRequestDto> players, Map<LolLine, RiftPlayerRequestDto> lineMap) {
        for (RiftPlayerRequestDto player : players) {
            for (RiftLinesRequestDto line : player.getLines()) {
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
    private boolean hasEmptyLines(Map<LolLine, RiftPlayerRequestDto> lineMap) {
        List<LolLine> requiredPositions = List.of(LolLine.values()); // Enum 값 그대로 사용
        return requiredPositions.stream().anyMatch(pos -> !lineMap.containsKey(pos));
    }

    // MainLine은 앞으로, SubLine은 뒤로
    private List<RiftPlayerRequestDto> soryTeamByLineRole(List<RiftPlayerRequestDto> players){
        return players.stream()
                .map(player -> {
                    // lines를 MainLine이 먼저 오도록 정렬
                    List<RiftLinesRequestDto> reorderedLines = player.getLines().stream()
                            .sorted((line1, line2) -> {
                                if (line1.getLineRole().isMainRole()) return -1; // MainLine을 앞에 배치
                                if (line2.getLineRole().isMainRole()) return 1;
                                return 0; // 그 외는 순서를 유지
                            })
                            .toList();

                    // Player의 lines를 재배열 후 새로운 Player 객체 생성 (불변 객체일 경우)
                    return new RiftPlayerRequestDto(player.getName(), player.getTier(),reorderedLines);
                })
                .collect(Collectors.toList());
    }



    private int calculateScore(List<RiftPlayerRequestDto> team) {
        return team.stream()
                .mapToInt(RiftPlayerRequestDto::getMmr)
                .sum();
    }

    private List<List<RiftPlayerRequestDto>> generateCombinations(List<RiftPlayerRequestDto> players, int size) {
        List<List<RiftPlayerRequestDto>> combinations = new ArrayList<>();
        generateCombinationsHelper(players, new ArrayList<>(), 0, size, combinations);
        return combinations;
    }

    private void generateCombinationsHelper(List<RiftPlayerRequestDto> players, List<RiftPlayerRequestDto> current, int index, int size, List<List<RiftPlayerRequestDto>> result) {
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

    public RiftTeamResponseDto create(List<RiftPlayerRequestDto> riftRequestDtos) {
        int retries = 10000; // 최대 시도 횟수
        while (retries > 0) {
            try {
                RiftTeamResponseDto one = splitTeam(riftRequestDtos);
                RiftTeamResponseDto two = generateBalanceByTier(one);
                RiftTeamResponseDto three = normalizeLineOrder(two);
                RiftTeamResponseDto four = checkLineAndRetryForLineBalance(three);
                return orderByLine(four);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }

    public RiftPlayerHistoryResponseDetailDto getDetailTeam(Long playerHistoryId) {
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryByPlayerHistoryId(playerHistoryId);
        return RiftPlayerHistoryResponseDetailDto.of(lolPlayerHistory);
    }

    public RiftPlayerResultHistoryResponseDetailDto getDetailResultTeam(Long playerResultHistoryId) {
        LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryByPlayerHistoryId(playerResultHistoryId);
        return RiftPlayerResultHistoryResponseDetailDto.of(lolPlayerResultHistory);
    }

    public Page<RiftPlayerHistoryResponseSimpleDto> getSimpleTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerHistoryByUser(user, pageable)
                .map(RiftPlayerHistoryResponseSimpleDto::of);
    }

    public Page<RiftPlayerResultHistoryResponseSimpleDto> getSimpleResultTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerResultHistoryByUser(user, pageable)
                .map(RiftPlayerResultHistoryResponseSimpleDto::of);
    }

    private Page<LolPlayerHistory> findLolPlayerHistoryByUser(User user, Pageable pageable) {
        return lolPlayerHistoryRepository.findByUser(user, pageable);
    }

    private Page<LolPlayerResultHistory> findLolPlayerResultHistoryByUser(User user, Pageable pageable) {
        return lolPlayerResultHistoryRepository.findByUser(user, pageable);
    }

    private LolPlayerHistory findLolPlayerHistoryByPlayerHistoryId(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

    private LolPlayerResultHistory findLolPlayerResultHistoryByPlayerHistoryId(Long playerResultistoryId) {
        return lolPlayerResultHistoryRepository.findById(playerResultistoryId).orElseThrow(() -> new LolException(LOL_RESULT_HISTORY_NOT_FOUND));
    }
}
