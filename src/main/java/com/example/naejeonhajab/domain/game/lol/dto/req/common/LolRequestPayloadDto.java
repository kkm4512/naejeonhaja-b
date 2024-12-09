package com.example.naejeonhajab.domain.game.lol.dto.req.common;

import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftRequestDto;

import java.util.List;

public interface LolRequestPayloadDto {
    String getPlayerHistoryTitle();
    List<RiftRequestDto> getRiftRequestDtos();
}
