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
        if (!hasUniqueLines(team.getTeamA()) || !hasUniqueLines(team.getTeamB())) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
    }

    private boolean hasUniqueLines(List<LolPlayerDto> players) {
        Set<LolLine> lines = new HashSet<>();
        for (LolPlayerDto p : players) {
            lines.add(p.getLines().get(0).getLine());
        }
        return lines.size() == players.size();
    }
}
