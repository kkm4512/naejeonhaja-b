package com.example.naejeonhajab;

import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionMasteryDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotLeagueDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotSummonerDto;
import com.example.naejeonhajab.domain.game.riot.service.RiotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
@ActiveProfiles("dev") // dev 프로파일 활성화
class RiotApiTest {

    @Autowired
    RiotService riotApiService;

    private static final String PUUID = "JwLOCeKRBJg2PUFr6LKTuGyBFlTW7JyLUXasOjGTQeZzjNeFMusWK10ozXkio76nXBbZiE5LgQOEjA";
    private static final String ID = "8YHvK0okEqXqa128F5r_L6_Op55dD5hiJgAI3Ofz0tgNow";

    @Test
    void test2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = riotApiService.getSummonersByPuuid(PUUID);
        RiotSummonerDto actual = objectMapper.readValue(response, RiotSummonerDto.class);
        assertEquals(PUUID,actual.getPuuid());
    }

    @Test
    void test3() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = riotApiService.getLeagueByid(ID);
        List<RiotLeagueDto> actual = objectMapper.readValue(response, new TypeReference<>() {});
        assertFalse(actual.isEmpty(), "리스트가 비어 있으면 안 됩니다");
    }

    @Test
    void test4() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = riotApiService.getMatchIdsByPuuid(PUUID);
        List<String> matchIds = objectMapper.readValue(response, new TypeReference<>() {});
        assertFalse(matchIds.isEmpty(), "리스트가 비어 있으면 안 됩니다");
    }

    @Test
    void test5() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = riotApiService.getChampionMasteryByPuuid(PUUID);
        List<RiotChampionMasteryDto> actual = objectMapper.readValue(response, new TypeReference<>() {});
        assertFalse(actual.isEmpty(), "리스트가 비어 있으면 안 됩니다");
    }
}
