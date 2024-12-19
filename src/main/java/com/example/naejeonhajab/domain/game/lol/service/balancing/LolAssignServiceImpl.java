package com.example.naejeonhajab.domain.game.lol.service.balancing;

import com.example.naejeonhajab.domain.game.lol.dto.common.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import com.example.naejeonhajab.domain.game.lol.enums.LolLineRole;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LolAssignServiceImpl {
    public LolAssignServiceImpl() {

    }

    // 두팀 모두 중복되지않은 라인을 가지고 있을경우 false 반환
    public LolTeamResponseDto assignLines(LolTeamResponseDto lolTeamResponseDto) {
        Map<LolLine, LolPlayerDto> teamALines = new HashMap<>();
        Map<LolLine, LolPlayerDto> teamBLines = new HashMap<>();
        assignTeamLines(lolTeamResponseDto.getTeamA(), teamALines);
        assignTeamLines(lolTeamResponseDto.getTeamB(), teamBLines);
        List<LolPlayerDto> lolPlayerADtos = updatePlayerLines(lolTeamResponseDto.getTeamA(), teamALines);
        List<LolPlayerDto> lolPlayerBDtos = updatePlayerLines(lolTeamResponseDto.getTeamB(), teamBLines);
        return LolTeamResponseDto.of(lolPlayerADtos,lolPlayerBDtos);
    }

    // 실질적인 중복 라인 제거하는 메서드
    private void assignTeamLines(List<LolPlayerDto> players, Map<LolLine, LolPlayerDto> lineMap) {
        for (LolPlayerDto player : players) {
            for (LolLinesDto line : player.getLines()) {
                if (lineMap.containsKey(line.getLine())) continue;

                // MAINLINE 처리
                if (line.getLineRole() == LolLineRole.MAINLINE) {
                    lineMap.put(line.getLine(), player);
                    break; // 한 유저는 한 라인만 배정
                }

                // SUBLINE 처리
                if (line.getLineRole() == LolLineRole.SUBLINE) {
                    lineMap.put(line.getLine(), player);
                    player.subtractionMmr(200); // SubLine 배정 시 티어 점수 -200
                    player.updateMmrReduced();
                    break; // 한 유저는 한 라인만 배정
                }
            }
        }
    }

    // Player의 Line 정보를 Map에 맞게 업데이트하고, 새로운 List<Player>를 반환하는 메서드
    public List<LolPlayerDto> updatePlayerLines(List<LolPlayerDto> riftPlayerRequestDtos, Map<LolLine, LolPlayerDto> lineMap) {
        return riftPlayerRequestDtos.stream()
                .peek(player ->
                        lineMap.forEach((assignedLine, assignedPlayer) -> {
                            if (player.getName().equals(assignedPlayer.getName())) {
                                // true라면 SUbLINE 배정
                                if (player.getMmrReduced()) {
                                    player.updateLines(List.of(new LolLinesDto(assignedLine, LolLineRole.SUBLINE)));
                                }
                                else {
                                    player.updateLines(List.of(new LolLinesDto(assignedLine, LolLineRole.MAINLINE)));
                                }
                                // Player의 Lines를 업데이트
                            }
                        })
                )
                .collect(Collectors.toList());
    }
}
