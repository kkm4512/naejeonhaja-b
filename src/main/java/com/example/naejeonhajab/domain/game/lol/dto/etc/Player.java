package com.example.naejeonhajab.domain.game.lol.dto.etc;

import com.example.naejeonhajab.domain.game.lol.dto.req.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.enums.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    String name;
    Tier tier;
    List<Lines> lines;
    int mmr;
    boolean isMmrReduced = false;

    public Player(String name, Tier tier, List<Lines> lines, int mmr) {
        this.name = name;
        this.tier = tier;
        this.lines = lines;
        this.mmr = mmr;
    }

    public static List<RiftRequestDto> of(Team team) {
        List<RiftRequestDto> riftRequestDtos = new ArrayList<>();
        List<RiftRequestDto> teamA = team.getTeamA().stream()
                .map(dto -> new RiftRequestDto(dto.getName(), dto.getTier(), dto.getLines()))
                .collect(Collectors.toList());
        List<RiftRequestDto> teamB = team.getTeamB().stream()
                .map(dto -> new RiftRequestDto(dto.getName(), dto.getTier(), dto.getLines()))
                .collect(Collectors.toList());
        riftRequestDtos.addAll(teamA);
        riftRequestDtos.addAll(teamB);
        return riftRequestDtos;
    }

    public void subtractionMmr(int minusMmr){
        this.mmr -= minusMmr;
    }

    public void updateLines(List<Lines> newLines) {
        this.lines = new ArrayList<>(newLines); // 기존 라인을 새 라인으로 덮어씀
    }

    public void updateMmrReduced(){
        this.isMmrReduced = true;
    }
}
