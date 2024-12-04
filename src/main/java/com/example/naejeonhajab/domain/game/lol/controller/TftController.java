package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.TftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.RiftResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.TftResponseDto;
import com.example.naejeonhajab.domain.game.lol.service.LolService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/game/lol/tft")
public class TftController {

    private final LolService lolService;

    public TftController(@Qualifier("tftServiceImpl") LolService lolService) {
        this.lolService = lolService;
    }

    @PostMapping
    public ApiResponse<TftResponseDto> createTeam(@RequestBody List<TftRequestDto> tftRequestDtos) {
        List<LolRequestDto> lolRequestDtos = new ArrayList<>(tftRequestDtos);
        TftResponseDto result = (TftResponseDto) lolService.createTeam(lolRequestDtos);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
