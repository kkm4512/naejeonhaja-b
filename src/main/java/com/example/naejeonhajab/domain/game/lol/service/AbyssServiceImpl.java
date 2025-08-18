package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.dto.common.Ids;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryUpdateRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerResultHistoryUpdateRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerResultHistory.LolPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistoryDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistorySimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistorySimpleDto;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultOutcome;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.game.lol.mapper.LolMapper;
import com.example.naejeonhajab.domain.game.lol.repository.playerHistory.LolPlayerHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.repository.resultHistory.LolPlayerResultHistoryRepository;
import com.example.naejeonhajab.domain.game.lol.service.balancing.LolBalanceServiceImpl;
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

@Slf4j
@RequiredArgsConstructor
@Service("abyssServiceImpl")
public class AbyssServiceImpl {
    // PlayerRepository
    private final LolPlayerHistoryRepository lolPlayerHistoryRepository;
    private final LolPlayerResultHistoryRepository lolPlayerResultHistoryRepository;

    // Mapper
    private final LolMapper lolMapper;

    // Balance
    private final LolBalanceServiceImpl lolBalanceService;

    // Util
    private final LolUtilService lolUtilService;

    // 10명의 인원으로, 5:5 팀 구성해준후 반환
    public LolTeamResponseDto createTeam(LolPlayerHistoryRequestDto dto) {
        return create(dto);
    }

    // 로그인한 사용자 플레이어 히스토리 내역 저장
    @Transactional
    public LolTeamResponseDto createTeamAndSavePlayerHistory(LolPlayerHistoryRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory playerHistory = LolPlayerHistory.from(dto,user, LolType.ABYSS);
        List<LolPlayer> playerList = LolPlayer.from(dto,playerHistory);
        lolMapper.insertPlayerHistory(playerHistory);
        lolMapper.insertPlayers(playerList);
        return create(dto);
    }

    // 내전 결과 저장 메서드
    @Transactional
    public void saveResultHistory(LolPlayerResultHistoryRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerResultHistory playerResultHistory = LolPlayerResultHistory.from(dto,user,LolType.ABYSS);
        LolPlayerResultOutcome playerResultOutcomeA = LolPlayerResultOutcome.from(dto.getTeamA(),playerResultHistory);
        LolPlayerResultOutcome playerResultOutcomeB = LolPlayerResultOutcome.from(dto.getTeamB(),playerResultHistory);
        List<LolPlayerResult> playerResultsA = LolPlayerResult.from(dto.getTeamA(),playerResultOutcomeA);
        List<LolPlayerResult> playerResultsB = LolPlayerResult.from(dto.getTeamB(),playerResultOutcomeB);
        lolMapper.inserPlayerResultHistory(playerResultHistory);
        lolMapper.insertPlayerResultOutcome(List.of(playerResultOutcomeA,playerResultOutcomeB));
        lolMapper.insertPlayersResult(playerResultsA);
        lolMapper.insertPlayersResult(playerResultsB);
    }

    // 10명의 유저를 받아, 5:5대전팀을 만들어주는 공통 메서드
    public LolTeamResponseDto create(LolPlayerHistoryRequestDto dto) {
        int retries = 10_000;
        lolUtilService.initMmr(dto.getLolPlayerDtos());
        while (retries > 0) {
            try {
                LolTeamResponseDto one = lolUtilService.splitTeam(dto.getLolPlayerDtos());
                return lolBalanceService.generateBalanceByTier(one);
            } catch (Exception e) {
                retries--;
            }
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }

    // 특정 ID의 플레이어 상세 히스토리 반환 (단일)
    @Transactional(readOnly = true)
    public LolPlayerHistoryDto getPlayerHistoryDetailTeam(Long playerHistoryId) {
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryByPlayerHistoryId(playerHistoryId);
        return LolPlayerHistoryDto.of(lolPlayerHistory);
    }

    // 특정 ID의 플레이어 대전 상세 내역 반환 (단일)
    @Transactional(readOnly = true)
    public LolPlayerResultHistoryDto getResultHistoryDetailTeam(Long playerResultHistoryId) {
        LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryByPlayerHistoryId(playerResultHistoryId);
        return LolPlayerResultHistoryDto.of(lolPlayerResultHistory);
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
    public Page<LolPlayerResultHistorySimpleDto> getResultHistorySimpleTeam(AuthUser authUser, Pageable pageable) {
        User user = User.of(authUser);
        return findLolPlayerResultHistoryByUser(user, pageable)
                .map(LolPlayerResultHistorySimpleDto::of);
    }

    // 플레이어 히스토리 내역 검색
    @Transactional(readOnly = true)
    public List<LolPlayerHistorySimpleDto> playerHistorySearch(String playerHistoryTitle, AuthUser authUser, Pageable pageable) {
        Page<LolPlayerHistory> pageResult = searchPlayerHistoryByTitle(pageable, playerHistoryTitle);
        List<LolPlayerHistory> listResult = pageResult.getContent();
        return LolPlayerHistorySimpleDto.of(listResult);
    }

    // 플레이어 대전결과 히스토리 내역 검색
    @Transactional(readOnly = true)
    public Page<LolPlayerResultHistorySimpleDto> playerResultHistorySearch(String playerResultHistoryTitle, AuthUser authUser, Pageable pageable) {
        Page<LolPlayerResultHistory> pageResult = searchPlayerResultHistoryByTitle(pageable, playerResultHistoryTitle);
        return pageResult.map(LolPlayerResultHistorySimpleDto::of);
    }

    @Transactional
    public void updatePlayerHistory(Long playerHistoryId, LolPlayerHistoryUpdateRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryById(playerHistoryId);
        user.isMe(lolPlayerHistory.getUser().getId());
        lolPlayerHistory.updatePlayerHistoryTitle(dto.getPlayerHistoryTitle());
    }

    @Transactional
    public void deletePlayerHistory(Long playerHistoryId, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryById(playerHistoryId);
        user.isMe(lolPlayerHistory.getUser().getId());
        lolPlayerHistoryRepository.delete(lolPlayerHistory);
    }

    @Transactional
    public void deletePlayerHistoryAll(@Valid List<Long> ids, AuthUser authUser) {
        User user = User.of(authUser);
        List<LolPlayerHistory> lolPlayerHistories = new ArrayList<>();
        for ( Long id : ids ) {
            LolPlayerHistory lolPlayerHistory = findLolPlayerHistoryById(id);
            user.isMe(lolPlayerHistory.getUser().getId());
            lolPlayerHistories.add(lolPlayerHistory);
        }
        lolPlayerHistoryRepository.deleteAll(lolPlayerHistories);
    }

    @Transactional
    public void updatePlayerResultHistory(Long playerResultHistoryId, @Valid LolPlayerResultHistoryUpdateRequestDto dto, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryById(playerResultHistoryId);
        user.isMe(lolPlayerResultHistory.getUser().getId());
        lolPlayerResultHistory.updatePlayerHistoryTitle(dto.getPlayerResultHistoryTitle());
    }

    @Transactional
    public void deletePlayerResultHistory(Long playerResultHistoryId, AuthUser authUser) {
        User user = User.of(authUser);
        LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryById(playerResultHistoryId);
        user.isMe(lolPlayerResultHistory.getUser().getId());
        lolPlayerResultHistoryRepository.delete(lolPlayerResultHistory);
    }

    @Transactional
    public void deleteAllPlayerResultHistory(@Valid List<LolPlayerResultHistorySimpleDto> dtos, AuthUser authUser) {
        User user = User.of(authUser);
        List<LolPlayerResultHistory> lolPlayerResultHistories = new ArrayList<>();
        for ( LolPlayerResultHistorySimpleDto dto : dtos ) {
            LolPlayerResultHistory lolPlayerResultHistory = findLolPlayerResultHistoryById(dto.getPlayerResultHistoryId());
            user.isMe(lolPlayerResultHistory.getUser().getId());
            lolPlayerResultHistories.add(lolPlayerResultHistory);
        }
        lolPlayerResultHistoryRepository.deleteAll(lolPlayerResultHistories);
    }


    // PlayerHistory
    @Transactional(readOnly = true)
    protected Page<LolPlayerHistory> findLolPlayerHistoryByUser(User user, Pageable pageable) {
        return lolPlayerHistoryRepository.findByUserAndType(user, LolType.ABYSS,pageable);
    }

    @Transactional(readOnly = true)
    protected LolPlayerHistory findLolPlayerHistoryById(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    protected LolPlayerHistory findLolPlayerHistoryByPlayerHistoryId(Long playerHistoryId) {
        return lolPlayerHistoryRepository.findById(playerHistoryId).orElseThrow(() -> new LolException(LOL_HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    protected Page<LolPlayerHistory> searchPlayerHistoryByTitle(Pageable pageable, String playerHistoryTitle) {
        return lolPlayerHistoryRepository.searchPlayerHistoryByTitle(LolType.ABYSS, pageable, playerHistoryTitle);
    }

    // PlayerResultHistory
    @Transactional(readOnly = true)
    protected Page<LolPlayerResultHistory> findLolPlayerResultHistoryByUser(User user, Pageable pageable) {
        return lolPlayerResultHistoryRepository.findByUserAndType(user, LolType.ABYSS,pageable);
    }

    @Transactional(readOnly = true)
    protected LolPlayerResultHistory findLolPlayerResultHistoryById(Long playerResultHistoryId) {
        return lolPlayerResultHistoryRepository.findById(playerResultHistoryId).orElseThrow(() -> new LolException(LOL_RESULT_HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    protected LolPlayerResultHistory findLolPlayerResultHistoryByPlayerHistoryId(Long playerResultistoryId) {
        return lolPlayerResultHistoryRepository.findById(playerResultistoryId).orElseThrow(() -> new LolException(LOL_RESULT_HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    protected Page<LolPlayerResultHistory> searchPlayerResultHistoryByTitle(Pageable pageable,String playerResultHistoryTitle) {
        return lolPlayerResultHistoryRepository.searchPlayerResultHistoryByTitle(LolType.ABYSS, pageable, playerResultHistoryTitle);
    }
}
