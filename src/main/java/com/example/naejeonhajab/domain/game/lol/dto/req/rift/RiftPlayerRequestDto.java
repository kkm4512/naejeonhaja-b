package com.example.naejeonhajab.domain.game.lol.dto.req.rift;

import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 클아이언트 요청으로 들어온 RiftRequestDto -> LolPlayer로 변경
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftPlayerRequestDto {
    String name;
    LolTier tier;
    List<RiftLinesRequestDto> lines;
    int mmr;
    boolean isMmrReduced = false;

    // 소환사의 협곡
    public RiftPlayerRequestDto(String name, LolTier tier, List<RiftLinesRequestDto> lines) {
        this.name = name;
        this.tier = tier;
        this.lines = lines;
        this.mmr = tier.getScore();
    }

    // 칼바람 나락
    public RiftPlayerRequestDto(String name, LolTier tier) {
        this.name = name;
        this.tier = tier;
        this.mmr = tier.getScore();
    }

    public static List<RiftPlayerRequestDto> of(RiftTeamResponseDto team) {
        List<RiftPlayerRequestDto> riftRequestDtos = new ArrayList<>();
        List<RiftPlayerRequestDto> teamA = team.getTeamA().stream()
                .map(dto -> new RiftPlayerRequestDto(dto.getName(), dto.getTier(), dto.getLines()))
                .collect(Collectors.toList());
        List<RiftPlayerRequestDto> teamB = team.getTeamB().stream()
                .map(dto -> new RiftPlayerRequestDto(dto.getName(), dto.getTier(), dto.getLines()))
                .collect(Collectors.toList());
        riftRequestDtos.addAll(teamA);
        riftRequestDtos.addAll(teamB);
        return riftRequestDtos;
    }


    public void subtractionMmr(int minusMmr){
        this.mmr -= minusMmr;
    }

    public void updateLines(List<RiftLinesRequestDto> newLines) {
        this.lines = new ArrayList<>(newLines); // 기존 라인을 새 라인으로 덮어씀
    }

    public void updateMmrReduced(){
        this.isMmrReduced = true;
    }
}
