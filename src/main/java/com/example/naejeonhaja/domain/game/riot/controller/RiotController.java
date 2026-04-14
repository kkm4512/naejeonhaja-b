package com.example.naejeonhaja.domain.game.riot.controller;

import com.example.naejeonhaja.common.response.ApiResponse;
import com.example.naejeonhaja.domain.game.riot.dto.*;
import com.example.naejeonhaja.domain.game.riot.service.RiotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game/lol/riot")
public class RiotController {

    private final RiotService riotService;

    @GetMapping("/player/{playerName}")
    public ApiResponse<RiotPlayerDto> getRiotPlayer(@PathVariable String playerName) {
        return riotService.getRiotPlayerByPlayerName(playerName);
    }

    @GetMapping("/player/{playerName}/basic")
    public ApiResponse<RiotPlayerBasicDto> getRiotPlayerBasic(@PathVariable String playerName) {
        return riotService.getRiotPlayerBasicByPlayerName(playerName);
    }

    @GetMapping("/account/{playerName}")
    public ApiResponse<RiotAccountDto> getAccount(@PathVariable String playerName) {
        return riotService.getAccountByPlayerName(playerName);
    }

    @GetMapping("/summoner/{puuid}")
    public ApiResponse<RiotSummonerDto> getSummoner(@PathVariable String puuid) {
        return riotService.getSummonersByPuuid(puuid);
    }

    @GetMapping("/league/{puuid}")
    public ApiResponse<RiotLeagueDto> getLeague(@PathVariable String puuid) {
        return riotService.getLeagueByPuuid(puuid);
    }

    @GetMapping("/champion-mastery/{puuid}")
    public ApiResponse<List<RiotChampionMasteryDto>> getChampionMastery(@PathVariable String puuid) {
        return riotService.getChampionMasteryByPuuid(puuid);
    }
}
