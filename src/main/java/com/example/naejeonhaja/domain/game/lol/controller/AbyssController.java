package com.example.naejeonhaja.domain.game.lol.controller;

import com.example.naejeonhaja.common.response.ApiResponse;
import com.example.naejeonhaja.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhaja.domain.game.lol.dto.req.LolPlayerRequestDto;
import com.example.naejeonhaja.domain.game.lol.service.AbyssService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.naejeonhaja.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lol")
public class AbyssController {

    private final AbyssService abyssService;

    @PostMapping("/abyss/team")
    public ApiResponse<LolTeamResponseDto> createTeam(@RequestBody @Valid LolPlayerRequestDto dto) {
        return ApiResponse.of(SUCCESS, abyssService.createTeam(dto));
    }
}
