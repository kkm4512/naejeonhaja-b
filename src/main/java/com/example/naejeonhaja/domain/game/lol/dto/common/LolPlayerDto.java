package com.example.naejeonhaja.domain.game.lol.dto.common;

import com.example.naejeonhaja.domain.game.lol.enums.LolTier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LolPlayerDto {
    @NotBlank(message = "이름은 공란일 수 없습니다")
    String name;
    @NotNull(message = "티어는 공란일 수 없습니다")
    LolTier tier;
    List<LolLinesDto> lines;
    Integer mmr;
    Boolean mmrReduced;

    public LolPlayerDto(String name, LolTier tier, List<LolLinesDto> lines, Integer mmr) {
        this.name = name;
        this.tier = tier;
        this.lines = lines;
        this.mmr = mmr;
    }

    public LolPlayerDto(String name, LolTier tier) {
        this.name = name;
        this.tier = tier;
    }

    public void updateMmr(int mmr) {
        this.mmr = mmr;
    }

    public void subtractionMmr(int minus) {
        this.mmr -= minus;
    }

    public void updateLines(List<LolLinesDto> newLines) {
        this.lines = new ArrayList<>(newLines);
    }

    public void updateMmrReduced() {
        this.mmrReduced = true;
    }
}
