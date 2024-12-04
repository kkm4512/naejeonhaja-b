package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.req.AbyssRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.AbyssResponseDto;
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

@RestController
@RequestMapping("/api/v1/game/lol/abyss")
public class AbyssController {

    private final LolService lolService;

    public AbyssController(@Qualifier("abyssServcieImpl") LolService lolService) {
        this.lolService = lolService;
    }

    @PostMapping
    public ApiResponse<AbyssResponseDto> createTeam(@RequestBody List<AbyssRequestDto> abyssRequestDtos) {
        List<LolRequestDto> lolRequestDtos = new ArrayList<>(abyssRequestDtos);
        AbyssResponseDto result = (AbyssResponseDto) lolService.createTeam(lolRequestDtos);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
