package com.example.naejeonhajab.domain.game.riot.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.riot.dto.RiotAccountDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionMasteryDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotLeagueDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotSummonerDto;
import com.example.naejeonhajab.domain.game.riot.service.RiotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "RiotController")
@RequestMapping("/api/v1/game/lol/riot")
public class RiotController {

    private final RiotService riotService;

    @GetMapping("/playerName/{playerName}")
    public ApiResponse<RiotAccountDto> getAccountByRiotId(
            @PathVariable("playerName") String playerName
    ) {
        return riotService.getAccountByRiotId(playerName);
    }

    @GetMapping("/puuid/{puuid}")
    public ApiResponse<RiotSummonerDto> getAccountByPuuid(
            @PathVariable("puuid") String puuid
    ) {
        return riotService.getSummonersByPuuid(puuid);
    }

    @GetMapping("/leagueId/{leagueId}")
    public ApiResponse<RiotLeagueDto> getLeagueByLeagueId(
            @PathVariable("leagueId") String leagueId
    ) {
        return riotService.getLeagueByid(leagueId);
    }

    @GetMapping("/puuid/{puuid}/champion")
    public ApiResponse<List<RiotChampionMasteryDto>> getChampionByPuuid(
            @PathVariable("puuid") String puuid
    ) {
        return riotService.getChampionMasteryByPuuid(puuid);
    }




}