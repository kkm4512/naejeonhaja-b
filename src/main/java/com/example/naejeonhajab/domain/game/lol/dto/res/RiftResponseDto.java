package com.example.naejeonhajab.domain.game.lol.dto.res;

import com.example.naejeonhajab.domain.game.lol.dto.etc.Player;
import com.example.naejeonhajab.domain.game.lol.dto.etc.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftResponseDto {
    private List<Player> teamA;
    private List<Player> teamB;

    public static RiftResponseDto of(Team team) {
        return new RiftResponseDto(team.getTeamA(), team.getTeamB());
    }
}

