package com.example.naejeonhaja.domain.game.lol.service.balancing;

import com.example.naejeonhaja.domain.game.lol.dto.common.LolLinesDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.enums.LolLine;
import com.example.naejeonhaja.domain.game.lol.enums.LolLineRole;
import com.example.naejeonhaja.domain.game.lol.service.util.LolUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LolAssignServiceImpl {

    private final LolUtilService lolUtilService;

    public LolTeamResponseDto assignLines(LolTeamResponseDto dto) {
        Map<LolLine, LolPlayerDto> teamAMap = new HashMap<>();
        Map<LolLine, LolPlayerDto> teamBMap = new HashMap<>();
        assignTeamLines(dto.getTeamA(), teamAMap);
        assignTeamLines(dto.getTeamB(), teamBMap);
        List<LolPlayerDto> updatedTeamA = updatePlayerLines(dto.getTeamA(), teamAMap);
        List<LolPlayerDto> updatedTeamB = updatePlayerLines(dto.getTeamB(), teamBMap);
        return LolTeamResponseDto.of(updatedTeamA, updatedTeamB, lolUtilService.calculateBalance(updatedTeamA, updatedTeamB));
    }

    private void assignTeamLines(List<LolPlayerDto> players, Map<LolLine, LolPlayerDto> lineMap) {
        Set<LolLine> assignedLines = new HashSet<>();
        for (LolPlayerDto player : players) {
            for (LolLinesDto playerLine : player.getLines()) {
                if (!assignedLines.contains(playerLine.getLine())) {
                    lineMap.put(playerLine.getLine(), player);
                    assignedLines.add(playerLine.getLine());
                    if (playerLine.getLineRole() == LolLineRole.SUBLINE) {
                        player.subtractionMmr(200);
                        player.updateMmrReduced();
                    }
                    break;
                }
            }
        }
        // 요청된 라인대로만 배정, 강제 할당하지 않음
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
