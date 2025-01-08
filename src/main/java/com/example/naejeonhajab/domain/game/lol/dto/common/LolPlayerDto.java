package com.example.naejeonhajab.domain.game.lol.dto.common;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.enums.LolTier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// 클아이언트 요청으로 들어온 RiftRequestDto -> LolPlayer로 변경
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LolPlayerDto {
    @NotBlank(message = "이름은 공란 일 수 없습니다")
    String name;
    @NotNull(message = "티어는 공란 일 수 없습니다")
    LolTier tier;
    List<LolLinesDto> lines;
    Integer mmr;
    Boolean mmrReduced;

    // 플레이어 히스토리내역 저장 DTO - 여기에는 mmrReduced가 필요하지않기 때문에 제외
    public LolPlayerDto(String name, LolTier tier, List<LolLinesDto> lines, Integer mmr) {
        this.name = name;
        this.tier = tier;
        this.lines = lines;
        this.mmr = mmr;
    }

    // 칼바람 나락
    public LolPlayerDto(String name, LolTier tier) {
        this.name = name;
        this.tier = tier;
    }

    public static List<LolPlayerDto> of(LolTeamResponseDto team) {
        List<LolPlayerDto> riftRequestDtos = new ArrayList<>();
        List<LolPlayerDto> teamA = team.getTeamA().stream()
                .map(dto -> new LolPlayerDto(dto.getName(), dto.getTier(), dto.getLines(), dto.getMmr(),dto.getMmrReduced()))
                .toList();
        List<LolPlayerDto> teamB = team.getTeamB().stream()
                .map(dto -> new LolPlayerDto(dto.getName(), dto.getTier(), dto.getLines(), dto.getMmr(),dto.getMmrReduced()))
                .toList();
        riftRequestDtos.addAll(teamA);
        riftRequestDtos.addAll(teamB);
        return riftRequestDtos;
    }

    public static List<LolPlayerDto> of(List<LolPlayerResult> playerResults) {
        return playerResults.stream()
                .map(dto -> new LolPlayerDto(dto.getName(), dto.getTier(), LolLinesDto.of(dto.getLines()), dto.getMmr(), dto.isMmrReduced()  ))
                .toList();
    }

    public void updateMmr(int plusMmr){
        this.mmr = plusMmr;
    }

    public void subtractionMmr(int minusMmr){
        this.mmr -= minusMmr;
    }

    public void updateLines(List<LolLinesDto> newLines) {
        this.lines = new ArrayList<>(newLines); // 기존 라인을 새 라인으로 덮어씀
    }

    public void updateMmrReduced(){
        this.mmrReduced = true;
    }

}

