package com.example.naejeonhaja.domain.game.lol.service;

import com.example.naejeonhaja.common.exception.BaseException;
import com.example.naejeonhaja.common.response.enums.BaseApiResponse;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.dto.req.LolPlayerRequestDto;
import com.example.naejeonhaja.domain.game.lol.service.balancing.LolAssignServiceImpl;
import com.example.naejeonhaja.domain.game.lol.service.balancing.LolBalanceServiceImpl;
import com.example.naejeonhaja.domain.game.lol.service.balancing.LolCheckServiceImpl;
import com.example.naejeonhaja.domain.game.lol.service.balancing.LolLineSortServiceImpl;
import com.example.naejeonhaja.domain.game.lol.service.util.LolUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiftService {

    private final LolUtilService lolUtilService;
    private final LolBalanceServiceImpl lolBalanceService;
    private final LolLineSortServiceImpl lolLineSortService;
    private final LolCheckServiceImpl lolCheckService;
    private final LolAssignServiceImpl lolAssignService;

    public LolTeamResponseDto createTeam(LolPlayerRequestDto dto) {
        int retries = 10_000;
        lolUtilService.initMmr(dto.getLolPlayers());
        while (retries-- > 0) {
            try {
                LolTeamResponseDto one = lolUtilService.splitTeam(dto.getLolPlayers());
                LolTeamResponseDto two = lolBalanceService.generateBalanceByTier(one);
                LolTeamResponseDto three = lolLineSortService.normalizeLineOrder(two);
                LolTeamResponseDto four = lolAssignService.assignLines(three);
                // lolCheckService.checkLine(four);
                return lolLineSortService.orderByLine(four);
            } catch (Exception ignored) {}
        }
        throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
    }
}
