package com.example.naejeonhajab.domain.game.dataDragon.service;

import com.example.naejeonhajab.common.exception.DataDragonException;
import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.dataDragon.dto.DataDragonChampionDto;
import com.example.naejeonhajab.domain.game.dataDragon.service.redis.DataDragonRedisService;
import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.example.naejeonhajab.common.response.enums.BaseApiResponse.SUCCESS;
import static com.example.naejeonhajab.common.response.enums.DataDragonApiResponse.*;

@Slf4j(topic = "DataDragonService")
@Service
@RequiredArgsConstructor
public class DataDragonService {
    private final DataDragonRedisService dataDragonRedisService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String DATA_DARGON_API_BASE_VERSION = "15.1.1";
    private static final String DATA_DRAGON_API_BASE_URL = "https://ddragon.leagueoflegends.com/cdn/"+ DATA_DARGON_API_BASE_VERSION + "/data/ko_KR";
    private static final String GET_CHAMPION_JSON_ENDPOINT = "/champion.json";


    // 챔피언의 고유 ID를 key에 저장하고, 그 데이터를 Redis에 저장함
    public void initChampionRedis(){
        if (dataDragonRedisService.existsChampionDto()) {
            return;
        }
        String url = DATA_DRAGON_API_BASE_URL + GET_CHAMPION_JSON_ENDPOINT;
        DataDragonChampionDto response = getDataDragonApiBaseMethod(url,DataDragonChampionDto.class);
        for (Map.Entry<String, RiotChampionDto> entry : response.getData().entrySet()) {
            dataDragonRedisService.setChampionDto(entry.getValue().getKey(),entry.getValue());
        }
    }

    public ApiResponse<RiotChampionDto> getChampionDtoByChampionId(String championId){
        RiotChampionDto response = dataDragonRedisService.getChampionDto(championId);
        return ApiResponse.of(SUCCESS,response);

    }

    // 단일 객체에 대해 클래스에 따른 타입 반환
    private <T> T getDataDragonApiBaseMethod(String url, Class<T> clazz){
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return objectMapper.readValue(response.getBody(),clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DataDragonException(DATA_DRAGON_API_BAD_REQUEST);
        }
    }

}
