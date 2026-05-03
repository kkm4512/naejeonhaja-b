package com.example.naejeonhaja.domain.game.lol.service.balancing;

import com.example.naejeonhaja.common.exception.BaseException;
import com.example.naejeonhaja.common.response.enums.BaseApiResponse;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.enums.LolLine;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LolCheckServiceImpl {

    public void checkLine(LolTeamResponseDto team) {
        if (!hasAllRequiredLines(team.getTeamA()) || !hasAllRequiredLines(team.getTeamB())) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
    }

    private boolean hasAllRequiredLines(List<LolPlayerDto> players) {
        Set<LolLine> lines = new HashSet<>();
        for (LolPlayerDto p : players) {
            if (!p.getLines().isEmpty()) {
                lines.add(p.getLines().get(0).getLine());
            }
        }

        // 각 팀에 TOP, JUNGLE, MID, AD, SUPPORT가 모두 있어야 함
        return lines.contains(LolLine.TOP) &&
                lines.contains(LolLine.JUNGLE) &&
                lines.contains(LolLine.MID) &&
                lines.contains(LolLine.AD) &&
                lines.contains(LolLine.SUPPORT) &&
                lines.size() == 5; // 중복 없이 정확히 5개 라인
    }
}
