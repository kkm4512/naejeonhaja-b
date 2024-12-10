package com.example.naejeonhajab.domain.game.lol.dto.req.rift;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftPlayerHistoryRequestDto {
    @NotBlank(message = "플레이어 히스토리 제목은 공백 일 수 없습니다")
    String playerHistoryTitle;
    List<RiftPlayerRequestDto> riftPlayerRequestDtos;
}
