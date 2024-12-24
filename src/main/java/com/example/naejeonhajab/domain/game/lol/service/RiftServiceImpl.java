package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryUpdateRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerResultHistory.LolPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistorySimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolLines;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultOutcome;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolResultLines;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.game.lol.mapper.LolMapper;
import com.example.naejeonhajab.domain.game.lol.repository.playerHistory.LolPlayerHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.repository.resultHistory.LolPlayerResultHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.service.balancing.LolAssignServiceImpl;
import com.example.naejeonhajab.domain.game.lol.service.balancing.LolBalanceServiceImpl;
import com.example.naejeonhajab.domain.game.lol.service.balancing.LolCheckServiceImpl;
import com.example.naejeonhajab.domain.game.lol.service.balancing.LolLineSortServiceImpl;
import com.example.naejeonhajab.domain.game.lol.service.util.LolUtilService;
import com.example.naejeonhajab.domain.user.entity.User;
import com.example.naejeonhajab.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_HISTORY_NOT_FOUND;
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_RESULT_HISTORY_NOT_FOUND;

@Slf4j(topic = "RiftServiceImpl")
@RequiredArgsConstructor
@Service("riftServiceImpl")
public class RiftServiceImpl {
    // PlayerRepository
    private final LolPlayerHistoryRepository lolPlayerHistoryRepository;
    private final LolPlayerResultHistoryRepository lolPlayerResultHistoryRepository;

    // Mapper
    private final LolMapper lolMapper;

    // Balance
    private final LolBalanceServiceImpl lolBalanceService;
    private final LolLineSortServiceImpl lolLineSortService;
    private final LolCheckServiceImpl lolCheckService;
    private final LolAssignServiceImpl lolAssignService;

    // Util
    private final LolUtilService lolUtilService;

    // 10명의 인원으로, 5:5 팀 구성해준후 반환
    public LolTeamResponseDto createTeam(LolPlayerHistoryRequestDto dto) {
        return create(dto);
    }

    @Transactional
    public LolTeamResponseDto createTeamAndSavePlayerHistory(LolPlayerHistoryRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory playerHistory = LolPlayerHistory.from(dto, user,LolType.RIFT);
        List<LolPlayer> playerList = LolPlayer.from(dto, playerHistory);
        List<LolLines> lines = LolLines.from(dto, playerList);
        lolMapper.insertPlayerHistory(playerHistory);
        lolMapper.insertPlayers(playerList);
        lolMapper.insertLines(lines);
        return create(dto);
    }


    // 내전 결과 저장 메서드
    @Transactional
    public void saveResultHistory(LolPlayerResultHistoryRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerResultHistory playerResultHistory = LolPlayerResultHistory.from(dto,user, LolType.RIFT);
        LolPlayerResultOutcome playerResultOutcomeA = LolPlayerResultOutcome.from(dto.getTeamA(),playerResultHistory);
        LolPlayerResultOutcome playerResultOutcomeB = LolPlayerResultOutcome.from(dto.getTeamB(),playerResultHistory);
        List<LolPlayerResult> playerResultsA = LolPlayerResult.from(dto.getTeamA(),playerResultOutcomeA);
        List<LolPlayerResult> playerResultsB = LolPlayerResult.from(dto.getTeamB(),playerResultOutcomeB);
        List<LolResultLines> linesA = LolResultLines.from(dto.getTeamA(),playerResultsA);
        List<LolResultLines> linesB = LolResultLines.from(dto.getTeamB(),playerResultsB);
        lolMapper.inserPlayerResultHistory(playerResultHistory);
        lolMapper.insertPlayerResultOutcome(List.of(playerResultOutcomeA,playerResultOutcomeB));
        lolMapper.insertPlayersResult(playerResultsA);
        lolMapper.insertPlayersResult(playerResultsB);
        lolMapper.insertResultLines(linesA);
        lolMapper.insertResultLines(linesB);
    }



    // 10명의 유저를 받아, 5:5대전팀을 만들어주는 공통 메서드
    private LolTeamResponseDto create(LolPlayerHistoryRequestDto dto) {
        int retries = 10_000;
        lolUtilService.initMmr(dto.getLolPlayerDtos());
        while (retries > 0) {
            try {
                LolTeamResponseDto one = lolUtilService.splitTeam(dto.getLolPlayerDtos());
                LolTeamResponseDto two = lolBalanceService.generateBalanceByTier(one);
                LolTeamResponseDto three = lolLineSortService.normalizeLineOrder(two);
                LolTeamResponseDto four = lolAssignService.assignLines(three);
                lolCheckService.checkLine(four);
                return lolLineSortService.orderByLine(four);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);

    }

    // 특정 ID의 플레이어 상세 히스토리 반환 (단일)
    @Transactional(readOnly = true)
    public LolPlayerHistoryResponseDetailDto getPlayerHistoryDetailTeam(Long playerHistoryId) {
        LolPlayerHistory lolPlayerHistory = searchPlayerHistoryByTitle(playerHistoryId);
        return LolPlayerHistoryResponseDetailDto.of(lolPlayerHistory);
    }

    // 특정 ID의 플레이어 대전 상세 내역 반환 (단일)
    @Transactional(readOnly = true)
    public LolPlayerResultHistoryResponseDetailDto getResultHistoryDetailTeam(Long playerResultHistoryId) {
        LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryByPlayerHistoryId(playerResultHistoryId);
        return LolPlayerResultHistoryResponseDetailDto.of(lolPlayerResultHistory);
    }

    // 현재 유저가 가지고있는 플레이어 히스토리 내역 조회 (다건)
    @Transactional(readOnly = true)
    public Page<LolPlayerHistorySimpleDto> getPlayerHistorySimpleTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerHistoryByUser(user, pageable)
                .map(LolPlayerHistorySimpleDto::of);
    }

    // 현재 유저가 가지고있는 대전 상세 내역 조회 (다건)
    @Transactional(readOnly = true)
    public Page<LolPlayerResultHistoryResponseSimpleDto> getResultHistorySimpleTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerResultHistoryByUser(user, pageable)
                .map(LolPlayerResultHistoryResponseSimpleDto::of);
    }

    // 플레이어 히스토리 내역 검색
    @Transactional(readOnly = true)
    public Page<LolPlayerHistorySimpleDto> playerHistorySearch(String playerHistoryTitle, AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        Page<LolPlayerHistory> pageResult = searchPlayerHistoryByTitle(user, pageable, playerHistoryTitle);
        return pageResult.map(LolPlayerHistorySimpleDto::of);
    }

    // 플레이어 대전결과 히스토리 내역 검색
    @Transactional(readOnly = true)
    public Page<LolPlayerResultHistoryResponseSimpleDto> playerResultHistorySearch(String playerResultHistoryTitle, AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        Page<LolPlayerResultHistory> pageResult = searchPlayerResultHistoryByTitle(user, pageable, playerResultHistoryTitle);
        return pageResult.map(LolPlayerResultHistoryResponseSimpleDto::of);
    }

    @Transactional
    public void updateTeam(Long playerHistoryId, LolPlayerHistoryUpdateRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryById(playerHistoryId);
        user.isMe(lolPlayerHistory.getUser().getId());
        lolPlayerHistory.updatePlayerHistoryTitle(dto.getPlayerHistoryTitle());
    }

    @Transactional
    public void deleteTeam(Long playerHistoryId, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryById(playerHistoryId);
        user.isMe(lolPlayerHistory.getUser().getId());
        lolPlayerHistoryRepository.delete(lolPlayerHistory);
    }

    @Transactional
    public void deleteAllTeam(@Valid List<LolPlayerHistorySimpleDto> dtos, AuthUser authUser) {
        User user = User.of(authUser);
        List<LolPlayerHistory> lolPlayerHistories = new ArrayList<>();
        for ( LolPlayerHistorySimpleDto dto : dtos ) {
            LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryById(dto.getPlayerHistoryId());
            user.isMe(lolPlayerHistory.getUser().getId());
            lolPlayerHistories.add(lolPlayerHistory);
        }
        lolPlayerHistoryRepository.deleteAll(lolPlayerHistories);
    }


    // PlayerHistory
    @Transactional(readOnly = true)
    protected Page<LolPlayerHistory> findLolPlayerHistoryByUser(User user, Pageable pageable) {
        return lolPlayerHistoryRepository.findByUserAndType(user, LolType.RIFT, pageable);
    }

    @Transactional(readOnly = true)
    public LolPlayerHistory findLolPlayerHistoryById(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    protected LolPlayerHistory searchPlayerHistoryByTitle(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    protected Page<LolPlayerHistory> searchPlayerHistoryByTitle(User user, Pageable pageable, String playerHistoryTitle) {
        return lolPlayerHistoryRepository.searchPlayerHistoryByTitle(user, LolType.RIFT, pageable, playerHistoryTitle);
    }

    // PlayerResultHistory
    @Transactional(readOnly = true)
    protected Page<LolPlayerResultHistory> findLolPlayerResultHistoryByUser(User user, Pageable pageable) {
        return lolPlayerResultHistoryRepository.findByUserAndType(user, LolType.RIFT,pageable);
    }

    @Transactional(readOnly = true)
    protected LolPlayerResultHistory findLolPlayerResultHistoryByPlayerHistoryId(Long playerResultistoryId) {
        return lolPlayerResultHistoryRepository.findById(playerResultistoryId).orElseThrow(() -> new LolException(LOL_RESULT_HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    protected Page<LolPlayerResultHistory> searchPlayerResultHistoryByTitle(User user, Pageable pageable,String playerResultHistoryTitle) {
        return lolPlayerResultHistoryRepository.searchPlayerResultHistoryByTitle(user, LolType.RIFT, pageable, playerResultHistoryTitle);
    }


}
