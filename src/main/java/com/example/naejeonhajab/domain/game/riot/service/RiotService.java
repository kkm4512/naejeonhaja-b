package com.example.naejeonhajab.domain.game.riot.service;

import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.LolApiResponse;
import com.example.naejeonhajab.domain.game.riot.dto.RiotAccountDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionMasteryDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotLeagueDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotSummonerDto;
import com.example.naejeonhajab.domain.game.riot.enums.LolRankType;
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
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.naejeonhajab.common.response.enums.BaseApiResponse.FAIL;
import static com.example.naejeonhajab.common.response.enums.BaseApiResponse.SUCCESS;
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_PLAYER_FOUND;
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_PLAYER_NOT_FOUND;

@Slf4j(topic = "RiotService")
@Service
@RequiredArgsConstructor
public class RiotService {

    @Value("${riot.api.key}")
    private String riotApiKey;

    private final RiotUtilService riotUtilService;

    private static final String RIOT_API_ASIA_BASE_URL = "https://asia.api.riotgames.com";
    private static final String RIOT_API_KR_BASE_URL = "https://kr.api.riotgames.com";


    private final RestTemplate restTemplate;

    public ApiResponse<RiotAccountDto> getAccountByRiotId(String playerName) {
        try {
                ObjectMapper objectMapper = new ObjectMapper();
                String[] split = riotUtilService.splitByShop(playerName);
                String gameName = split[0];
                String tagLine = split[1];
                String url = RIOT_API_ASIA_BASE_URL + "/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine;
                String response = getRiotApiBaseMethod(url);
                RiotAccountDto result = objectMapper.readValue(response, RiotAccountDto.class);
                return ApiResponse.of(LOL_PLAYER_FOUND,result);
            }
            catch (Exception e) {
                log.error(e.getMessage());
                return ApiResponse.of(LOL_PLAYER_NOT_FOUND);
        }
    }

    // /lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}

    public ApiResponse<RiotSummonerDto> getSummonersByPuuid(String puuid) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String url = RIOT_API_KR_BASE_URL + "/lol/summoner/v4/summoners/by-puuid/" + puuid;
            String response = getRiotApiBaseMethod(url);
            RiotSummonerDto result = objectMapper.readValue(response, RiotSummonerDto.class);
            return ApiResponse.of(SUCCESS,result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.of(FAIL);
        }
    }

    // /lol/league/v4/entries/by-summoner/{encryptedSummonerId}

    public ApiResponse<RiotLeagueDto> getLeagueByid(String id) {
        // Riot API Endpoint
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String url = RIOT_API_KR_BASE_URL + "/lol/league/v4/entries/by-summoner/" + id;
            String response = getRiotApiBaseMethod(url);
            List<RiotLeagueDto> result = objectMapper.readValue(response, new TypeReference<>() {});
            Optional<RiotLeagueDto> findSoloRankType = result.stream()
                    .filter(r -> r.getQueueType().equals(LolRankType.RANKED_SOLO_5x5))
                    .findFirst();
            return ApiResponse.of(SUCCESS,findSoloRankType.orElseThrow(() -> new Exception(FAIL.getMessage())));
        } catch (Exception e) {
            return ApiResponse.of(FAIL);
        }
    }

    // /lol/champion-mastery/v4/champion-masteries/by-puuid/{encryptedPUUID}
    public ApiResponse<List<RiotChampionMasteryDto>> getChampionMasteryByPuuid(String puuid) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String url = RIOT_API_KR_BASE_URL + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid;
            String response = getRiotApiBaseMethod(url);
            List<RiotChampionMasteryDto> result = objectMapper.readValue(response, new TypeReference<>() {});
            result.sort(Comparator.comparingInt(RiotChampionMasteryDto::getChampionPoints).reversed());
            return ApiResponse.of(SUCCESS,result.stream().limit(3).toList());
        } catch (Exception e) {
            return ApiResponse.of(FAIL);
        }
    }

    private String getRiotApiBaseMethod(String url){
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey); // Riot API 키를 헤더에 추가

        // HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch account information from Riot API: " + e.getMessage(), e);
        }
    }
}