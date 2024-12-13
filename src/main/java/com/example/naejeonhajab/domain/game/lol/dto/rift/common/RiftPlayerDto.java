package com.example.naejeonhajab.domain.game.lol.dto.rift.common;

import com.example.naejeonhajab.domain.game.lol.dto.rift.res.RiftTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// 클아이언트 요청으로 들어온 RiftRequestDto -> LolPlayer로 변경
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftPlayerDto {
    @NotBlank(message = "이름은 공란 일 수 없습니다")
    String name;
    @NotNull(message = "티어는 공란 일 수 없습니다")
    LolTier tier;
    @Valid
    @Size(min = 1, message = "라인은 최소 1개 이상 선택해야 합니다")
    List<RiftLinesDto> lines;
    Integer mmr;
    boolean mmrReduced;

    // 소환사의 협곡 - playerHistory
    public RiftPlayerDto(String name, LolTier tier, List<RiftLinesDto> lines) {
        this.name = name;
        this.tier = tier;
        this.lines = lines;
        this.mmr = tier.getScore();
    }

    // 소환사의 협곡 - 결과
    public RiftPlayerDto(String name, LolTier tier, List<RiftLinesDto> lines, boolean isMmrReduced ) {
        this.mmrReduced = isMmrReduced;
        this.lines = lines;
        this.tier = tier;
        this.name = name;
    }

    // 칼바람 나락
    public RiftPlayerDto(String name, LolTier tier) {
        this.name = name;
        this.tier = tier;
        this.mmr = tier.getScore();
    }

    public static List<RiftPlayerDto> of(RiftTeamResponseDto team) {
        List<RiftPlayerDto> riftRequestDtos = new ArrayList<>();
        List<RiftPlayerDto> teamA = team.getTeamA().stream()
                .map(dto -> new RiftPlayerDto(dto.getName(), dto.getTier(), dto.getLines()))
                .toList();
        List<RiftPlayerDto> teamB = team.getTeamB().stream()
                .map(dto -> new RiftPlayerDto(dto.getName(), dto.getTier(), dto.getLines()))
                .toList();
        riftRequestDtos.addAll(teamA);
        riftRequestDtos.addAll(teamB);
        return riftRequestDtos;
    }

    public static List<RiftPlayerDto> of(List<LolPlayerResult> playerResults) {
        return playerResults.stream()
                .map(dto -> new RiftPlayerDto(dto.getName(), dto.getTier(), RiftLinesDto.of(dto.getLines()), dto.getMmr(), dto.isMmrReduced()  ))
                .toList();
    }

    public void updateMmr(int plusMmr){
        this.mmr = plusMmr;
    }

    public void subtractionMmr(int minusMmr){
        this.mmr -= minusMmr;
    }

    public void updateLines(List<RiftLinesDto> newLines) {
        this.lines = new ArrayList<>(newLines); // 기존 라인을 새 라인으로 덮어씀
    }

    public void updateMmrReduced(){
        this.mmrReduced = true;
    }

}

