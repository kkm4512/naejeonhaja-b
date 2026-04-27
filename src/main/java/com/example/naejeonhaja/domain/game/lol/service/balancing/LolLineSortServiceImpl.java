package com.example.naejeonhaja.domain.game.lol.service.balancing;

import com.example.naejeonhaja.domain.game.lol.dto.common.LolLinesDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.enums.LolLine;
import com.example.naejeonhaja.domain.game.lol.service.util.LolUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LolLineSortServiceImpl {

    private final LolUtilService lolUtilService;

    public LolTeamResponseDto normalizeLineOrder(LolTeamResponseDto team) {
        List<LolPlayerDto> teamA = sortByLineRole(team.getTeamA());
        List<LolPlayerDto> teamB = sortByLineRole(team.getTeamB());
        return LolTeamResponseDto.of(teamA, teamB, lolUtilService.calculateBalance(teamA, teamB));
    }

    private List<LolPlayerDto> sortByLineRole(List<LolPlayerDto> players) {
        return players.stream()
                .map(p -> {
                    List<LolLinesDto> sorted = p.getLines().stream()
                            .sorted((a, b) -> a.getLineRole().isMainRole() ? -1 : 1)
                            .toList();
                    return new LolPlayerDto(p.getName(), p.getTier(), sorted, p.getMmr(), p.getMmrReduced());
                })
                .collect(Collectors.toList());
    }

    public LolTeamResponseDto orderByLine(LolTeamResponseDto team) {
        List<LolLine> order = List.of(LolLine.values());
        List<LolPlayerDto> teamA = sortByLine(team.getTeamA(), order);
        List<LolPlayerDto> teamB = sortByLine(team.getTeamB(), order);
        return LolTeamResponseDto.of(teamA, teamB, lolUtilService.calculateBalance(teamA, teamB));
    }

    private List<LolPlayerDto> sortByLine(List<LolPlayerDto> players, List<LolLine> order) {
        return players.stream()
                .sorted(Comparator.comparingInt(p -> order.indexOf(p.getLines().get(0).getLine())))
                .collect(Collectors.toList());
    }
}
