package com.example.naejeonhajab.domain.game.lol.dto.etc;

import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestPayloadDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayerHistory;
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
public class LolPlayerDto {
    String name;
    LolTier tier;
    List<LolLinesDto> lines;
    int mmr;
    boolean isMmrReduced = false;

    // 소환사의 협곡
    public LolPlayerDto(String name, LolTier tier, List<LolLinesDto> lines, int mmr) {
        this.name = name;
        this.tier = tier;
        this.lines = lines;
        this.mmr = mmr;
    }

    // 칼바람 나락
    public LolPlayerDto(String name, LolTier tier, int mmr) {
        this.name = name;
        this.tier = tier;
        this.mmr = mmr;
    }

    public static List<RiftRequestDto> of(LolTeamDto team) {
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

    public static List<LolPlayer> from(LolRequestPayloadDto lolRequestPayloadDto, LolPlayerHistory playerHistory) {
        List<LolPlayer> players = new ArrayList<>();

        for ( RiftRequestDto riftRequestDto : lolRequestPayloadDto.getRiftRequestDtos() ) {
            players.add( new com.example.naejeonhajab.domain.game.lol.entity.LolPlayer(
                    null,
                    playerHistory,
                    riftRequestDto.getName(),
                    riftRequestDto.getTier(),
                    riftRequestDto.getTier().getScore(),
                    null
            ));
        }

        return players;
    }


    public void subtractionMmr(int minusMmr){
        this.mmr -= minusMmr;
    }

    public void updateLines(List<LolLinesDto> newLines) {
        this.lines = new ArrayList<>(newLines); // 기존 라인을 새 라인으로 덮어씀
    }

    public void updateMmrReduced(){
        this.isMmrReduced = true;
    }
}
