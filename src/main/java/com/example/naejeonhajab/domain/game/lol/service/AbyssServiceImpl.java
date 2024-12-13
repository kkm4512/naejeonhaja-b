package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.abyss.common.AbyssPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.abyss.req.AbyssPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.abyss.req.AbyssPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.abyss.res.AbyssPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.abyss.res.AbyssPlayerResultHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.abyss.res.AbyssTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.res.LolPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.res.LolPlayerResultHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultOutcome;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.game.lol.repository.player.LolPlayerHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.repository.player.LolPlayerRepository;
import com.example.naejeonhajab.domain.game.lol.repository.result.LolPlayerResultHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.repository.result.LolPlayerResultOutcomeRepository;
import com.example.naejeonhajab.domain.game.lol.repository.result.LolPlayerResultRepository;
import com.example.naejeonhajab.domain.user.entity.User;
import com.example.naejeonhajab.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_HISTORY_NOT_FOUND;
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_RESULT_HISTORY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service("abyssServiceImpl")
public class AbyssServiceImpl {
    // Player
    private final LolPlayerHistoryRepository lolPlayerHistoryRepository;
    private final LolPlayerRepository lolPlayerRepository;

    // Result
    private final LolPlayerResultHistoryRepository lolPlayerResultHistoryRepository;
    private final LolPlayerResultRepository lolPlayerResultRepository;
    private final LolPlayerResultOutcomeRepository lolPlayerResultOutcomeRepository;

    private static int retries = 10000; // 최대 시도 횟수

    // 10명의 인원으로, 5:5 팀 구성해준후 반환
    public AbyssTeamResponseDto createTeam(AbyssPlayerHistoryRequestDto dto) {
        return create(dto);
    }

    // 로그인한 사용자 플레이어 히스토리 내역 저장
    @Transactional
    public AbyssTeamResponseDto createPlayerHistoryAndTeam(AbyssPlayerHistoryRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory playerHistory = LolPlayerHistory.from(dto,user);
        lolPlayerHistoryRepository.save(playerHistory);
        List<LolPlayer> playerList = LolPlayer.from(dto,playerHistory);
        lolPlayerRepository.saveAll(playerList);
        return create(dto);
    }

    // 내전 결과 저장 메서드
    @Transactional
    public void createResultTeam(AbyssPlayerResultHistoryRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerResultHistory playerResultHistory = LolPlayerResultHistory.fromAbyssPlayerResultHistoryRequestDto(dto,user);
        lolPlayerResultHistoryRepository.save(playerResultHistory);
        LolPlayerResultOutcome playerResultOutcomeA = LolPlayerResultOutcome.fromAbyssTeamResultdDtoAndLolPlayerResultHistory(dto.getTeamA(),playerResultHistory);
        LolPlayerResultOutcome playerResultOutcomeB = LolPlayerResultOutcome.fromAbyssTeamResultdDtoAndLolPlayerResultHistory(dto.getTeamB(),playerResultHistory);
        lolPlayerResultOutcomeRepository.saveAll(List.of(playerResultOutcomeA,playerResultOutcomeB));
        List<LolPlayerResult> playerResultsA = LolPlayerResult.fromAbyssTeamResultDtoAndLolPlayerResultOutcome(dto.getTeamA(),playerResultOutcomeA);
        List<LolPlayerResult> playerResultsB = LolPlayerResult.fromAbyssTeamResultDtoAndLolPlayerResultOutcome(dto.getTeamB(),playerResultOutcomeB);
        lolPlayerResultRepository.saveAll(playerResultsA);
        lolPlayerResultRepository.saveAll(playerResultsB);
    }

    // 팀을 랜덤하게 5명으로 나눔
    public AbyssTeamResponseDto splitTeam(List<AbyssPlayerDto> riftPlayerRequestDtos) {
        List<AbyssPlayerDto> teamA = new ArrayList<>();
        List<AbyssPlayerDto> teamB = new ArrayList<>();
        try {
            Collections.shuffle(riftPlayerRequestDtos);
            for ( int i=0; i<riftPlayerRequestDtos.size()/2; i++ ){
                teamA.add(riftPlayerRequestDtos.get(i));
                teamB.add(riftPlayerRequestDtos.get(i + (riftPlayerRequestDtos.size()/2)));
            }
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }

        return AbyssTeamResponseDto.of(teamA,teamB);
    }

    // 티어를 기준으로 5:5 팀 나누기
    public AbyssTeamResponseDto generateBalanceByTier(AbyssTeamResponseDto team) {
        List<AbyssPlayerDto> bestTeamA = new ArrayList<>();
        List<AbyssPlayerDto> bestTeamB = new ArrayList<>();
        try {
            List<AbyssPlayerDto> allPlayers = new ArrayList<>();
            allPlayers.addAll(team.getTeamA());
            allPlayers.addAll(team.getTeamB());
            // 모든 조합 탐색
            int n = allPlayers.size() / 2;
            List<List<AbyssPlayerDto>> allCombinations = generateCombinations(allPlayers, n);

            int minDifference = Integer.MAX_VALUE;


            // 각 조합에 대해 점수 차이를 계산
            for (List<AbyssPlayerDto> teamA : allCombinations) {
                List<AbyssPlayerDto> teamB = new ArrayList<>(allPlayers);
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
        return AbyssTeamResponseDto.of(bestTeamA, bestTeamB);
    }

    // 각 팀의 mmr 총합 계산
    private int calculateScore(List<AbyssPlayerDto> team) {
        return team.stream()
                .mapToInt(AbyssPlayerDto::getMmr)
                .sum();
    }

    // 두 팀중 최적의 mmr점수 차이가 적은 조합을 찾는 메서드
    private List<List<AbyssPlayerDto>> generateCombinations(List<AbyssPlayerDto> players, int size) {
        List<List<AbyssPlayerDto>> combinations = new ArrayList<>();
        generateCombinationsHelper(players, new ArrayList<>(), 0, size, combinations);
        return combinations;
    }

    // 두 팀중 최적의 mmr점수 차이가 적은 조합을 찾는 메서드 실질적으로 이행하는 메서드
    private void generateCombinationsHelper(List<AbyssPlayerDto> players, List<AbyssPlayerDto> current, int index, int size, List<List<AbyssPlayerDto>> result) {
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
    public AbyssTeamResponseDto create(AbyssPlayerHistoryRequestDto dto) {
        initMmr(dto.getPlayerDtos());
        while (retries > 0) {
            try {
                AbyssTeamResponseDto one = splitTeam(dto.getPlayerDtos());
                return generateBalanceByTier(one);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }

    // 10명의 유저에 대해 티어에 따른 MMR 부여 메서드
    private void initMmr(List<AbyssPlayerDto> riftRequestDtos){
        for (AbyssPlayerDto riftPlayerRequestDto : riftRequestDtos) {
            riftPlayerRequestDto.updateMmr(riftPlayerRequestDto.getTier().getScore());
        }
    }

    // 특정 ID의 플레이어 상세 히스토리 반환 (단일)
    public AbyssPlayerHistoryResponseDetailDto getDetailTeam(Long playerHistoryId) {
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryByPlayerHistoryId(playerHistoryId);
        return AbyssPlayerHistoryResponseDetailDto.of(lolPlayerHistory);
    }

    // 특정 ID의 플레이어 대전 상세 내역 반환 (단일)
    public AbyssPlayerResultHistoryResponseDetailDto getDetailResultTeam(Long playerResultHistoryId) {
        LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryByPlayerHistoryId(playerResultHistoryId);
        return AbyssPlayerResultHistoryResponseDetailDto.of(lolPlayerResultHistory);
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
        return lolPlayerHistoryRepository.findByUserAndType(user, LolType.ABYSS,pageable);
    }

    private Page<LolPlayerResultHistory> findLolPlayerResultHistoryByUser(User user, Pageable pageable) {
        return lolPlayerResultHistoryRepository.findByUserAndType(user, LolType.ABYSS,pageable);
    }

    private LolPlayerHistory findLolPlayerHistoryByPlayerHistoryId(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

    private LolPlayerResultHistory findLolPlayerResultHistoryByPlayerHistoryId(Long playerResultistoryId) {
        return lolPlayerResultHistoryRepository.findById(playerResultistoryId).orElseThrow(() -> new LolException(LOL_RESULT_HISTORY_NOT_FOUND));
    }
}
