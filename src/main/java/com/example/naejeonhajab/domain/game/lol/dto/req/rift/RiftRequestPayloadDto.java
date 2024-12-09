package com.example.naejeonhajab.domain.game.lol.dto.req.rift;

import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestPayloadDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiftRequestPayloadDto implements LolRequestPayloadDto {
    String playerHistoryTitle;
    List<RiftRequestDto> riftRequestDtos;

}
