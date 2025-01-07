package com.example.naejeonhajab.domain.game.dataDragon.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.dataDragon.dto.DataDragonChampionDto;
import com.example.naejeonhajab.domain.game.dataDragon.service.DataDragonService;
import com.example.naejeonhajab.domain.game.riot.service.RiotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "RiotController")
@RequestMapping("/api/v1/game/lol/dataDragon")
public class DataDragonController {

    private final DataDragonService dataDragonService;

    @GetMapping("/championId/{championId}")
    public ApiResponse<DataDragonChampionDto.ChampionDto> getChampionByChampionId(
            @PathVariable("championId") String championId
    ) {
        return dataDragonService.getChampionDtoByChampionId(championId);
    }



}
