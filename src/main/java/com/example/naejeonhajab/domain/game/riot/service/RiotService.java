package com.example.naejeonhajab.domain.game.riot.service;

import com.example.naejeonhajab.annotation.GetRiotPlayerBasicStore;
import com.example.naejeonhajab.annotation.GetRiotPlayerStore;
import com.example.naejeonhajab.common.exception.DataDragonException;
import com.example.naejeonhajab.common.exception.RiotException;
import com.example.naejeonhajab.common.helper.UrlEncodingHelper;
import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.DataDragonApiResponse;
import com.example.naejeonhajab.common.response.enums.RiotApiResponse;
import com.example.naejeonhajab.domain.game.dataDragon.service.DataDragonService;
import com.example.naejeonhajab.domain.game.riot.dto.*;
import com.example.naejeonhajab.domain.game.riot.enums.LolRankType;
import com.example.naejeonhajab.domain.game.riot.service.redis.RiotRedisService;
import com.example.naejeonhajab.domain.game.riot.service.util.RiotUtilService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.naejeonhajab.common.response.enums.BaseApiResponse.SUCCESS;
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_PLAYER_FOUND;
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_PLAYER_NOT_FOUND;

@Slf4j(topic = "RiotService")
@Service
@RequiredArgsConstructor
public class RiotService {

    @Value("${riot.api.key}")
    private String riotApiKey;

    private final ObjectMapper objectMapper;


    private final RiotUtilService riotUtilService;
    private final DataDragonService dataDragonService;
    private final RiotRedisService riotRedisService;

    private static final String RIOT_API_ASIA_BASE_URL = "https://asia.api.riotgames.com";
    private static final String RIOT_API_KR_BASE_URL = "https://kr.api.riotgames.com";
    private static final String GET_ACCOUNT_BY_RIOT_ID_ENDPOINT = "/riot/account/v1/accounts/by-riot-id";
    private static final String GET_SUMMONER_BY_PUUID_ENDPOINT = "/lol/summoner/v4/summoners/by-puuid";
    private static final String GET_LEAGUE_BY_LEAGUE_ENDPOINT = "/lol/league/v4/entries/by-puuid";
    private static final String GET_CHAMPION_MASTERY_BY_PUUID_ENDPOINT = "/lol/champion-mastery/v4/champion-masteries/by-puuid";

    private final RestTemplate restTemplate;

    @Transactional
    @GetRiotPlayerStore
    public ApiResponse<RiotPlayerDto> getRiotPlayerByPlayerName(String playerName) {
        RiotAccountDto riotAccountDto = getAccountByPlayerName(playerName).getData();
        RiotSummonerDto riotSummonerDto = getSummonersByPuuid(riotAccountDto.getPuuid()).getData();
        RiotLeagueDto riotLeagueDto = getLeagueByid(riotAccountDto.getPuuid()).getData();
        List<RiotChampionMasteryDto> riotChampionMasteryDtos = getChampionMasteryByPuuid(riotSummonerDto.getPuuid()).getData();
        List<RiotChampionDto> championDtos = riotChampionMasteryDtos.stream()
                .map(r -> dataDragonService.getChampionDtoByChampionId(String.valueOf(r.getChampionId())).getData())
                .toList();
        RiotPlayerDto riotPlayerDto = new RiotPlayerDto(
                riotAccountDto,
                riotSummonerDto,
                riotChampionMasteryDtos,
                riotLeagueDto,
                championDtos
        );

        riotRedisService.setRiotPlayerDto(playerName, riotPlayerDto);
        return ApiResponse.of(LOL_PLAYER_FOUND,riotPlayerDto);
    }

    @GetRiotPlayerBasicStore
    public ApiResponse<RiotPlayerBasicDto> getRiotPlayerBasicByPlayerName(String playerName) {
        try {
            RiotAccountDto riotAccountDto = getAccountByPlayerName(playerName).getData();
            RiotSummonerDto riotSummonerDto = getSummonersByPuuid(riotAccountDto.getPuuid()).getData();
            RiotLeagueDto riotLeagueDto = getLeagueByid(riotAccountDto.getPuuid()).getData();
            RiotPlayerBasicDto riotPlayerBasicDto = new RiotPlayerBasicDto(
                    riotAccountDto,
                    riotSummonerDto,
                    riotLeagueDto
            );
            riotRedisService.setRiotPlayerBasicDto(playerName, riotPlayerBasicDto);
            return ApiResponse.of(LOL_PLAYER_FOUND,riotPlayerBasicDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ApiResponse.of(LOL_PLAYER_NOT_FOUND);
        }
    }

    public ApiResponse<RiotAccountDto> getAccountByPlayerName(String playerName) {
        String[] split = riotUtilService.splitByShop(playerName);
        String gameName = UrlEncodingHelper.encodeToUrlConditionally(split[0]);
        String tagLine = UrlEncodingHelper.encodeToUrlConditionally(split[1]);
        String url = RIOT_API_ASIA_BASE_URL + GET_ACCOUNT_BY_RIOT_ID_ENDPOINT;

        // 2) 경로 세그먼트로 안전하게 조립 (자동 %인코딩)
        URI uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .pathSegment(gameName, tagLine)   // ‘#’ 포함 금지. 둘로 분리!
                .build(true)                      // 이미 세그먼트 단위로 안전
                .toUri();

        RiotAccountDto response = getRiotApiBaseMethod(uri, RiotAccountDto.class);
        return ApiResponse.of(LOL_PLAYER_FOUND,response);
    }

    // /lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}

    public ApiResponse<RiotSummonerDto> getSummonersByPuuid(String puuid) {
        String url = RIOT_API_KR_BASE_URL + GET_SUMMONER_BY_PUUID_ENDPOINT;
        URI uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .pathSegment(puuid)   // ‘#’ 포함 금지. 둘로 분리!
                .build(true)                      // 이미 세그먼트 단위로 안전
                .toUri();
        RiotSummonerDto response = getRiotApiBaseMethod(uri,RiotSummonerDto.class);
        return ApiResponse.of(SUCCESS,response);
    }

    // /lol/league/v4/entries/by-summoner/{encryptedSummonerId}

    public ApiResponse<RiotLeagueDto> getLeagueByid(String puuid) {
        // Riot API Endpoint
        String url = RIOT_API_KR_BASE_URL + GET_LEAGUE_BY_LEAGUE_ENDPOINT;

        URI uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .pathSegment(puuid)   // ‘#’ 포함 금지. 둘로 분리!
                .build(true)                      // 이미 세그먼트 단위로 안전
                .toUri();

        List<RiotLeagueDto> response = getRiotApiBaseMethod(uri,new TypeReference<>() {});
        Optional<RiotLeagueDto> findSoloRankType = response.stream()
                .filter(r -> r.getQueueType().equals(LolRankType.RANKED_SOLO_5x5))
                .findFirst();
        return ApiResponse.of(SUCCESS,findSoloRankType.orElse(null));
    }

    // /lol/champion-mastery/v4/champion-masteries/by-puuid/{encryptedPUUID}
    public ApiResponse<List<RiotChampionMasteryDto>> getChampionMasteryByPuuid(String puuid) {
        String url = RIOT_API_KR_BASE_URL + GET_CHAMPION_MASTERY_BY_PUUID_ENDPOINT;

        URI uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .pathSegment(puuid)   // ‘#’ 포함 금지. 둘로 분리!
                .build(true)                      // 이미 세그먼트 단위로 안전
                .toUri();

        List<RiotChampionMasteryDto> response = getRiotApiBaseMethod(uri,new TypeReference<>() {});
        response.sort(Comparator.comparingInt(RiotChampionMasteryDto::getChampionPoints).reversed());
        return ApiResponse.of(SUCCESS,response.stream().limit(3).toList());
    }

    // 단일 객체에 대해 클래스에 따른 타입 반환
    private <T> T getRiotApiBaseMethod(URI uri, Class<T> clazz){
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey); // Riot API 키를 헤더에 추가

        // HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return objectMapper.readValue(response.getBody(),clazz) ;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    // 리스트 타입에 대한 데이터 반환
    private <T> T getRiotApiBaseMethod(URI uri, TypeReference<T> typeReference){
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey); // Riot API 키를 헤더에 추가

        // HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return objectMapper.readValue(response.getBody(),typeReference) ;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
