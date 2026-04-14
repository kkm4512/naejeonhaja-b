package com.example.naejeonhaja.domain.game.dataDragon.controller;

import com.example.naejeonhaja.common.response.ApiResponse;
import com.example.naejeonhaja.domain.game.dataDragon.service.DataDragonService;
import com.example.naejeonhaja.domain.game.riot.dto.RiotChampionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.naejeonhaja.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game/lol/dataDragon")
public class DataDragonController {

    private final DataDragonService dataDragonService;

    @GetMapping("/champion/{championId}")
    public ApiResponse<RiotChampionDto> getChampion(@PathVariable String championId) {
        return ApiResponse.of(SUCCESS, dataDragonService.getChampionById(championId));
    }

    @PostMapping("/reload")
    public ApiResponse<Void> reloadChampions() {
        dataDragonService.loadChampions();
        return ApiResponse.of(SUCCESS);
    }
}
