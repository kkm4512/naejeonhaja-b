package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.LolResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.RiftResponseDto;
import com.example.naejeonhajab.domain.game.lol.service.LolService;
import com.example.naejeonhajab.domain.game.lol.service.RiftServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/game/lol/rift")
public class RiftController {

    private final LolService lolService;

    public RiftController(@Qualifier("riftServiceImpl") LolService lolService) {
        this.lolService = lolService;
    }

    @PostMapping
    public ApiResponse<RiftResponseDto> createTeam(@RequestBody List<RiftRequestDto> riftRequestDtos) {
        List<LolRequestDto> lolRequestDtos = new ArrayList<>(riftRequestDtos);
        RiftResponseDto result = (RiftResponseDto) lolService.createTeam(lolRequestDtos);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
