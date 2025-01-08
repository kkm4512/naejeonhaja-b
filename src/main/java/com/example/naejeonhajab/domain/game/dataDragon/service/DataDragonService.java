package com.example.naejeonhajab.domain.game.dataDragon.service;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.dataDragon.dto.DataDragonChampionDto;
import com.example.naejeonhajab.domain.game.dataDragon.service.redis.DataDragonRedisService;
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

@Slf4j(topic = "DataDragonService")
@Service
@RequiredArgsConstructor
public class DataDragonService {
    private final DataDragonRedisService dataDragonRedisService;
    private final RestTemplate restTemplate;
    private static final String DATA_DRAGON_API_BASE_URL = "https://ddragon.leagueoflegends.com/cdn/14.24.1/data/ko_KR";

    public void initChampionRedis(){
        try {
            if (dataDragonRedisService.existsChampionDto()) {
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String url = DATA_DRAGON_API_BASE_URL + "/champion.json";
            String response = getDataDragonApiBaseMethod(url);
            DataDragonChampionDto championData = objectMapper.readValue(response, DataDragonChampionDto.class);
            for (Map.Entry<String, DataDragonChampionDto.ChampionDto> entry : championData.getData().entrySet()) {
                dataDragonRedisService.setChampionDto(entry.getValue().getKey(),entry.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch account information from Riot API: " + e.getMessage(), e);
        }
    }

    public ApiResponse<DataDragonChampionDto.ChampionDto> getChampionDtoByChampionId(String championId){
        DataDragonChampionDto.ChampionDto response = dataDragonRedisService.getChampionDto(championId);
        return ApiResponse.of(SUCCESS,response);

    }

    private String getDataDragonApiBaseMethod(String url){
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
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch account information from Riot API: " + e.getMessage(), e);
        }
    }
}
