package com.example.naejeonhajab.domain.game.lol.service.balancing;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.LolTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LolCheckServiceImpl {
    // 라인이 각자 올바르게 구성되어있는지 확인
    public void checkLine(LolTeamResponseDto team) {
        try {
            boolean flagA = hasEmptyLines(team.getTeamA());
            boolean flagB = hasEmptyLines(team.getTeamB());
            // 둘중
            if (!(flagA && flagB)) {
                throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
            }
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
    }

    // 존재하지않는 것이 하나라도 있으면 true 반환
    private boolean hasEmptyLines(List<LolPlayerDto> playerDto) {
        Set<LolLine> line = new HashSet<>();
        for (LolPlayerDto player : playerDto) {
            line.add(player.getLines().get(0).getLine());
        }
        return line.size() == playerDto.size();
    }

}
