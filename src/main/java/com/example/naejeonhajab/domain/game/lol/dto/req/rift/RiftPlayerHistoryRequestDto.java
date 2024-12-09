package com.example.naejeonhajab.domain.game.lol.dto.req.rift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftPlayerHistoryRequestDto {
    String playerHistoryTitle;
    List<RiftPlayerRequestDto> riftPlayerRequestDtos;

}
