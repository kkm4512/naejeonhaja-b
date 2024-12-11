package com.example.naejeonhajab.domain.game.lol.dto.req.rift.result;

import com.example.naejeonhajab.common.enums.Outcome;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftTeamResultRequestDto {
    @NotNull(message = "어느팀이 승리했는지 골라 주세요")
    Outcome outcome;
    @Valid
    List<RiftPlayerRequestDto> team;
}
