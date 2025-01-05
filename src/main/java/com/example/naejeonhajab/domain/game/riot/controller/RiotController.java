package com.example.naejeonhajab.domain.game.riot.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
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
@RequestMapping("/api/v1/game/lol/riot")
public class RiotController {

    private final RiotService riotService;

    @GetMapping("/{playerName}")
    public ApiResponse<Void> getAccountByRiotId(@PathVariable("playerName") String playerName) {
        log.info("Riot Controller 들어왔군요");
        return riotService.getAccountByRiotId(playerName);
    }
}
