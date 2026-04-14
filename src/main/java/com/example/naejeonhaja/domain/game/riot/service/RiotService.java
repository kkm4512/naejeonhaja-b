package com.example.naejeonhaja.domain.game.riot.service;

import com.example.naejeonhaja.common.helper.UrlEncodingHelper;
import com.example.naejeonhaja.common.response.ApiResponse;
import com.example.naejeonhaja.domain.game.dataDragon.service.DataDragonService;
import com.example.naejeonhaja.domain.game.riot.dto.*;
import com.example.naejeonhaja.domain.game.riot.enums.LolRankType;
import com.example.naejeonhaja.domain.game.riot.service.util.RiotUtilService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.naejeonhaja.common.response.enums.BaseApiResponse.SUCCESS;
import static com.example.naejeonhaja.common.response.enums.LolApiResponse.LOL_PLAYER_FOUND;
import static com.example.naejeonhaja.common.response.enums.LolApiResponse.LOL_PLAYER_NOT_FOUND;

@Slf4j(topic = "RiotService")
@Service
@RequiredArgsConstructor
public class RiotService {

    @Value("${riot.api.key}")
    private String riotApiKey;

    private final ObjectMapper objectMapper;
    private final RiotUtilService riotUtilService;
    private final DataDragonService dataDragonService;
    private final RestTemplate restTemplate;

    private static final String RIOT_ASIA_BASE = "https://asia.api.riotgames.com";
    private static final String RIOT_KR_BASE = "https://kr.api.riotgames.com";

    public ApiResponse<RiotPlayerDto> getRiotPlayerByPlayerName(String playerName) {
        try {
            RiotAccountDto account = getAccountByPlayerName(playerName).getData();
            RiotSummonerDto summoner = getSummonersByPuuid(account.getPuuid()).getData();
            RiotLeagueDto league = getLeagueByPuuid(account.getPuuid()).getData();
            List<RiotChampionMasteryDto> masteries = getChampionMasteryByPuuid(summoner.getPuuid()).getData();
            List<RiotChampionDto> champions = masteries.stream()
                    .map(m -> dataDragonService.getChampionById(String.valueOf(m.getChampionId())))
                    .toList();
            return ApiResponse.of(LOL_PLAYER_FOUND, new RiotPlayerDto(account, summoner, masteries, league, champions));
        } catch (Exception e) {
            log.error("getRiotPlayer 실패: {}", e.getMessage());
            return ApiResponse.of(LOL_PLAYER_NOT_FOUND, null);
        }
    }

    public ApiResponse<RiotPlayerBasicDto> getRiotPlayerBasicByPlayerName(String playerName) {
        try {
            RiotAccountDto account = getAccountByPlayerName(playerName).getData();
            RiotSummonerDto summoner = getSummonersByPuuid(account.getPuuid()).getData();
            RiotLeagueDto league = getLeagueByPuuid(account.getPuuid()).getData();
            return ApiResponse.of(LOL_PLAYER_FOUND, new RiotPlayerBasicDto(account, summoner, league));
        } catch (Exception e) {
            log.error("getRiotPlayerBasic 실패: {}", e.getMessage());
            return ApiResponse.of(LOL_PLAYER_NOT_FOUND, null);
        }
    }

    public ApiResponse<RiotAccountDto> getAccountByPlayerName(String playerName) {
        String[] parts = riotUtilService.splitByShop(playerName);
        String gameName = UrlEncodingHelper.encodeToUrlConditionally(parts[0]);
        String tagLine = UrlEncodingHelper.encodeToUrlConditionally(parts[1]);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(RIOT_ASIA_BASE + "/riot/account/v1/accounts/by-riot-id")
                .pathSegment(gameName, tagLine)
                .build(true)
                .toUri();
        return ApiResponse.of(SUCCESS, call(uri, RiotAccountDto.class));
    }

    public ApiResponse<RiotSummonerDto> getSummonersByPuuid(String puuid) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(RIOT_KR_BASE + "/lol/summoner/v4/summoners/by-puuid")
                .pathSegment(puuid)
                .build(true).toUri();
        return ApiResponse.of(SUCCESS, call(uri, RiotSummonerDto.class));
    }

    public ApiResponse<RiotLeagueDto> getLeagueByPuuid(String puuid) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(RIOT_KR_BASE + "/lol/league/v4/entries/by-puuid")
                .pathSegment(puuid)
                .build(true).toUri();
        List<RiotLeagueDto> list = callList(uri, new TypeReference<>() {});
        Optional<RiotLeagueDto> solo = list.stream()
                .filter(r -> r.getQueueType() == LolRankType.RANKED_SOLO_5x5)
                .findFirst();
        return ApiResponse.of(SUCCESS, solo.orElse(null));
    }

    public ApiResponse<List<RiotChampionMasteryDto>> getChampionMasteryByPuuid(String puuid) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(RIOT_KR_BASE + "/lol/champion-mastery/v4/champion-masteries/by-puuid")
                .pathSegment(puuid)
                .build(true).toUri();
        List<RiotChampionMasteryDto> list = callList(uri, new TypeReference<>() {});
        list.sort(Comparator.comparingInt(RiotChampionMasteryDto::getChampionPoints).reversed());
        return ApiResponse.of(SUCCESS, list.stream().limit(3).toList());
    }

    private <T> T call(URI uri, Class<T> clazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Riot-Token", riotApiKey);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            return objectMapper.readValue(response.getBody(), clazz);
        } catch (Exception e) {
            log.error("Riot API 호출 실패: {}", e.getMessage());
            return null;
        }
    }

    private <T> T callList(URI uri, TypeReference<T> ref) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Riot-Token", riotApiKey);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            return objectMapper.readValue(response.getBody(), ref);
        } catch (Exception e) {
            log.error("Riot API 호출 실패: {}", e.getMessage());
            return null;
        }
    }
}
