package com.example.naejeonhajab.domain.game.lol.dto.rift.req;

import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftTeamResultDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftPlayerResultHistoryRequestDto {
    @NotBlank(message = "대전결과 제목은 공백 일 수 없습니다")
    String playerResultHistoryTitle;
    @Valid
    RiftTeamResultDto teamA;
    @Valid
    RiftTeamResultDto teamB;
}
