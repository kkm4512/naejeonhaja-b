package com.example.naejeonhaja.domain.game.dataDragon.service;

import com.example.naejeonhaja.domain.game.dataDragon.dto.DataDragonChampionDto;
import com.example.naejeonhaja.domain.game.riot.dto.RiotChampionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "DataDragonService")
@Service
@RequiredArgsConstructor
public class DataDragonService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Redis 없이 메모리에 챔피언 데이터 캐싱
    private final Map<String, RiotChampionDto> championCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            loadChampions();
        } catch (Exception e) {
            log.warn("DataDragon 초기화 실패 (서버 기동 후 재시도 가능): {}", e.getMessage());
        }
    }

    public void loadChampions() {
        String version = getLatestVersion();
        String url = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/champion.json";
        DataDragonChampionDto response = fetchFromUrl(url, DataDragonChampionDto.class);
        if (response != null && response.getData() != null) {
            response.getData().forEach((name, dto) -> championCache.put(dto.getKey(), dto));
            log.info("DataDragon 챔피언 데이터 로드 완료 (버전: {}, 챔피언 수: {})", version, championCache.size());
        }
    }

    public RiotChampionDto getChampionById(String championId) {
        return championCache.get(championId);
    }

    public String getLatestVersion() {
        String[] versions = fetchFromUrl("https://ddragon.leagueoflegends.com/api/versions.json", String[].class);
        return (versions != null && versions.length > 0) ? versions[0] : "14.1.1";
    }

    private <T> T fetchFromUrl(String url, Class<T> clazz) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readValue(response.getBody(), clazz);
        } catch (Exception e) {
            log.error("DataDragon API 호출 실패: {}", e.getMessage());
            return null;
        }
    }
}
