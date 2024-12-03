package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.req.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.RiftResponseDto;
import com.example.naejeonhajab.domain.game.lol.service.RiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game/lol/rift")
@RequiredArgsConstructor
public class RiftController {
    private final RiftService riftService;

    @PostMapping
    public ApiResponse<RiftResponseDto> createTeam(@RequestBody List<RiftRequestDto> riftRequestDtos) {
        RiftResponseDto result = riftService.createTeam(riftRequestDtos);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
