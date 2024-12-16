package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.*;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolLines;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultOutcome;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolResultLines;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.game.lol.mapper.LolMapper;
import com.example.naejeonhajab.domain.game.lol.repository.player.LolPlayerHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.repository.result.LolPlayerResultHistoryRepository;
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

@Slf4j(topic = "RiftServiceImpl")
@RequiredArgsConstructor
@Service("riftServiceImpl")
public class RiftServiceImpl {
    // Player
    private final LolPlayerHistoryRepository lolPlayerHistoryRepository;
    private final LolPlayerResultHistoryRepository lolPlayerResultHistoryRepository;
    private final LolMapper riftMapper;
    private static int retries = 10000; // 최대 시도 횟수

    // 10명의 인원으로, 5:5 팀 구성해준후 반환
    public LolTeamResponseDto createTeam(LolPlayerHistoryRequestDto dto) {
        return create(dto);
    }

    @Transactional
    public LolTeamResponseDto createPlayerHistoryAndTeam(LolPlayerHistoryRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory playerHistory = LolPlayerHistory.from(dto, user,LolType.RIFT);
        List<LolPlayer> playerList = LolPlayer.from(dto, playerHistory);
        List<LolLines> lines = LolLines.from(dto, playerList);
        riftMapper.insertPlayerHistory(playerHistory);
        riftMapper.insertPlayers(playerList);
        riftMapper.insertLines(lines);
        return create(dto);
    }


    // 내전 결과 저장 메서드
    @Transactional
    public void createResultTeam(LolPlayerResultHistoryRequestDto riftPlayerResultHistoryRequestDto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerResultHistory playerResultHistory = LolPlayerResultHistory.from(riftPlayerResultHistoryRequestDto,user, LolType.RIFT);
        LolPlayerResultOutcome playerResultOutcomeA = LolPlayerResultOutcome.from(riftPlayerResultHistoryRequestDto.getTeamA(),playerResultHistory);
        LolPlayerResultOutcome playerResultOutcomeB = LolPlayerResultOutcome.from(riftPlayerResultHistoryRequestDto.getTeamB(),playerResultHistory);
        List<LolPlayerResult> playerResultsA = LolPlayerResult.from(riftPlayerResultHistoryRequestDto.getTeamA(),playerResultOutcomeA);
        List<LolPlayerResult> playerResultsB = LolPlayerResult.from(riftPlayerResultHistoryRequestDto.getTeamB(),playerResultOutcomeB);
        List<LolResultLines> linesA = LolResultLines.from(riftPlayerResultHistoryRequestDto.getTeamA(),playerResultsA);
        List<LolResultLines> linesB = LolResultLines.from(riftPlayerResultHistoryRequestDto.getTeamB(),playerResultsB);
        riftMapper.inserPlayerResultHistory(playerResultHistory);
        riftMapper.insertPlayerResultOutcome(List.of(playerResultOutcomeA,playerResultOutcomeB));
        riftMapper.insertPlayersResult(playerResultsA);
        riftMapper.insertPlayersResult(playerResultsB);
        riftMapper.insertResultLines(linesA);
        riftMapper.insertResultLines(linesB);
    }

    // 팀을 랜덤하게 5명으로 나눔
    public LolTeamResponseDto splitTeam(List<LolPlayerDto> riftPlayerRequestDtos) {
        List<LolPlayerDto> teamA = new ArrayList<>();
        List<LolPlayerDto> teamB = new ArrayList<>();
    try {
        Collections.shuffle(riftPlayerRequestDtos);
        for ( int i=0; i<riftPlayerRequestDtos.size()/2; i++ ){
            teamA.add(riftPlayerRequestDtos.get(i));
            teamB.add(riftPlayerRequestDtos.get(i + (riftPlayerRequestDtos.size()/2)));
        }
    } catch (Exception e) {
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }

        return new LolTeamResponseDto(teamA, teamB);
    }

    // 티어를 기준으로 5:5 팀 나누기
    public LolTeamResponseDto generateBalanceByTier(LolTeamResponseDto team) {
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
        return new LolTeamResponseDto(bestTeamA, bestTeamB);
    }

    // MainLine으로 정렬된 새로운 팀을 반환
    private LolTeamResponseDto normalizeLineOrder(LolTeamResponseDto team) {
        List<LolPlayerDto> teamA;
        List<LolPlayerDto> teamB;
        try {
            teamA = soryTeamByLineRole(team.getTeamA());
            teamB = soryTeamByLineRole(team.getTeamB());
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return new LolTeamResponseDto(teamA, teamB);
    }

    // 라인이 각자 올바르게 구성되어있는지 확인
    private LolTeamResponseDto checkLineAndRetryForLineBalance(LolTeamResponseDto team) {
        LolTeamResponseDto newTeam;
        Map<LolLine, LolPlayerDto> teamALines = new HashMap<>();
        Map<LolLine, LolPlayerDto> teamBLines = new HashMap<>();
        try {
            boolean hasEmptyLines = assignLines(team,teamALines,teamBLines);

            List<LolPlayerDto> teamA = updatePlayerLines(team.getTeamA(), teamALines);
            List<LolPlayerDto> teamB = updatePlayerLines(team.getTeamB(), teamBLines);
            newTeam = new LolTeamResponseDto(teamA, teamB);

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
    // 라인순서대로 정리 (전체적)
    private LolTeamResponseDto orderByLine(LolTeamResponseDto team) {
        List<LolPlayerDto> sortedTeamA;
        List<LolPlayerDto> sortedTeamB;
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
        return new LolTeamResponseDto(sortedTeamA, sortedTeamB);
    }

    // 실제 라인 정렬 담당 함수
    private List<LolPlayerDto> sortTeamByLine(List<LolPlayerDto> riftPlayerRequestDtos, List<LolLine> lineOrder) {
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
    private List<LolPlayerDto> updatePlayerLines(List<LolPlayerDto> riftPlayerRequestDtos, Map<LolLine, LolPlayerDto> lineMap) {
        return riftPlayerRequestDtos.stream()
                .peek(player ->
                        lineMap.forEach((assignedLine, assignedPlayer) -> {
                            if (player.getName().equals(assignedPlayer.getName())) {
                                // true라면 SUbLINE 배정
                                if (player.getMmrReduced()) {
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


    // 중복된 라인 제거하고, 서브라인이라면 mmr 200점 차감시킴 (전체적)
    // 두팀 모두 중복되지않은 라인을 가지고 있을경우 false 반환
    private boolean assignLines(LolTeamResponseDto team, Map<LolLine, LolPlayerDto> teamALines, Map<LolLine, LolPlayerDto> teamBLines) {
        assignTeamLines(team.getTeamA(), teamALines);
        assignTeamLines(team.getTeamB(), teamBLines);
        return hasEmptyLines(teamALines) || hasEmptyLines(teamBLines);
    }

    // 실질적인 중복 라인 제거하는 메서드
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


    // 존재하지않는 것이 하나라도 있으면 true 반환
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
                    return new LolPlayerDto(player.getName(), player.getTier(),reorderedLines,player.getMmr(),player.getMmrReduced());
                })
                .collect(Collectors.toList());
    }


    // 각 팀의 mmr 총합 계산
    private int calculateScore(List<LolPlayerDto> team) {
        return team.stream()
                .mapToInt(LolPlayerDto::getMmr)
                .sum();
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

    // 10명의 유저를 받아, 5:5대전팀을 만들어주는 공통 메서드
    public LolTeamResponseDto create(LolPlayerHistoryRequestDto dto) {
        initMmr(dto.getLolPlayerDtos());
        while (retries > 0) {
            try {
                LolTeamResponseDto one = splitTeam(dto.getLolPlayerDtos());
                LolTeamResponseDto two = generateBalanceByTier(one);
                LolTeamResponseDto three = normalizeLineOrder(two);
                LolTeamResponseDto four = checkLineAndRetryForLineBalance(three);
                return orderByLine(four);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }

    // 10명의 유저에 대해 티어에 따른 MMR 부여 메서드
    private void initMmr(List<LolPlayerDto> riftRequestDtos){
        for (LolPlayerDto riftPlayerRequestDto : riftRequestDtos) {
            riftPlayerRequestDto.updateMmr(riftPlayerRequestDto.getTier().getScore());
        }
    }

    // 특정 ID의 플레이어 상세 히스토리 반환 (단일)
    public LolPlayerHistoryResponseDetailDto getDetailTeam(Long playerHistoryId) {
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryByPlayerHistoryId(playerHistoryId);
        return LolPlayerHistoryResponseDetailDto.of(lolPlayerHistory);
    }

    // 특정 ID의 플레이어 대전 상세 내역 반환 (단일)
    public LolPlayerResultHistoryResponseDetailDto getDetailResultTeam(Long playerResultHistoryId) {
        LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryByPlayerHistoryId(playerResultHistoryId);
        return LolPlayerResultHistoryResponseDetailDto.of(lolPlayerResultHistory);
    }

    // 현재 유저가 가지고있는 플레이어 히스토리 내역 조회 (다건)
    public Page<LolPlayerHistoryResponseSimpleDto> getSimpleTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerHistoryByUser(user, pageable)
                .map(LolPlayerHistoryResponseSimpleDto::of);
    }

    // 현재 유저가 가지고있는 대전 상세 내역 조회 (다건)
    public Page<LolPlayerResultHistoryResponseSimpleDto> getSimpleResultTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerResultHistoryByUser(user, pageable)
                .map(LolPlayerResultHistoryResponseSimpleDto::of);
    }


    private Page<LolPlayerHistory> findLolPlayerHistoryByUser(User user, Pageable pageable) {
        return lolPlayerHistoryRepository.findByUserAndType(user, LolType.RIFT, pageable);
    }

    private Page<LolPlayerResultHistory> findLolPlayerResultHistoryByUser(User user, Pageable pageable) {
        return lolPlayerResultHistoryRepository.findByUserAndType(user, LolType.RIFT,pageable);
    }

    private LolPlayerHistory findLolPlayerHistoryByPlayerHistoryId(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

    private LolPlayerResultHistory findLolPlayerResultHistoryByPlayerHistoryId(Long playerResultistoryId) {
        return lolPlayerResultHistoryRepository.findById(playerResultistoryId).orElseThrow(() -> new LolException(LOL_RESULT_HISTORY_NOT_FOUND));
    }
}
