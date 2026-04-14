package com.example.naejeonhaja.domain.game.lol.service.balancing;

import com.example.naejeonhaja.domain.game.lol.dto.common.LolLinesDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.enums.LolLine;
import com.example.naejeonhaja.domain.game.lol.enums.LolLineRole;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LolAssignServiceImpl {

    public LolTeamResponseDto assignLines(LolTeamResponseDto dto) {
        Map<LolLine, LolPlayerDto> teamAMap = new HashMap<>();
        Map<LolLine, LolPlayerDto> teamBMap = new HashMap<>();
        assignTeamLines(dto.getTeamA(), teamAMap);
        assignTeamLines(dto.getTeamB(), teamBMap);
        return LolTeamResponseDto.of(
                updatePlayerLines(dto.getTeamA(), teamAMap),
                updatePlayerLines(dto.getTeamB(), teamBMap)
        );
    }

    private void assignTeamLines(List<LolPlayerDto> players, Map<LolLine, LolPlayerDto> lineMap) {
        for (LolPlayerDto player : players) {
            for (LolLinesDto line : player.getLines()) {
                if (lineMap.containsKey(line.getLine())) continue;
                if (line.getLineRole() == LolLineRole.MAINLINE) {
                    lineMap.put(line.getLine(), player);
                    break;
                }
                if (line.getLineRole() == LolLineRole.SUBLINE) {
                    lineMap.put(line.getLine(), player);
                    player.subtractionMmr(200);
                    player.updateMmrReduced();
                    break;
                }
            }
        }
    }

    private List<LolPlayerDto> updatePlayerLines(List<LolPlayerDto> players, Map<LolLine, LolPlayerDto> lineMap) {
        return players.stream()
                .peek(player -> lineMap.forEach((line, assigned) -> {
                    if (player.getName().equals(assigned.getName())) {
                        LolLineRole role = Boolean.TRUE.equals(player.getMmrReduced())
                                ? LolLineRole.SUBLINE : LolLineRole.MAINLINE;
                        player.updateLines(List.of(new LolLinesDto(line, role)));
                    }
                }))
                .collect(Collectors.toList());
    }
}
