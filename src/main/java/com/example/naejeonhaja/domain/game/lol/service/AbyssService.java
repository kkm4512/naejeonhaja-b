package com.example.naejeonhaja.domain.game.lol.service;

import com.example.naejeonhaja.common.exception.BaseException;
import com.example.naejeonhaja.common.response.enums.BaseApiResponse;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.dto.req.LolPlayerRequestDto;
import com.example.naejeonhaja.domain.game.lol.service.balancing.LolBalanceServiceImpl;
import com.example.naejeonhaja.domain.game.lol.service.util.LolUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbyssService {

    private final LolUtilService lolUtilService;
    private final LolBalanceServiceImpl lolBalanceService;

    // 칼바람은 라인 배정 없이 MMR 밸런싱만
    public LolTeamResponseDto createTeam(LolPlayerRequestDto dto) {
        int retries = 10_000;
        lolUtilService.initMmr(dto.getLolPlayers());
        while (retries-- > 0) {
            try {
                LolTeamResponseDto one = lolUtilService.splitTeam(dto.getLolPlayers());
                return lolBalanceService.generateBalanceByTier(one);
            } catch (Exception ignored) {}
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }
}
