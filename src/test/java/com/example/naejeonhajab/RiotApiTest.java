package com.example.naejeonhajab;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.riot.dto.RiotAccountDto;
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

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("dev") // dev 프로파일 활성화
class RiotApiTest {

    @Autowired
    RiotService riotApiService;

    private static final String PUUID = "py2n4Hgjkwq-jwLe1Fz3nQFDSBfKaWXNSAvFyy924quxAgm0SMeHGePTkKo56jD__aDn8qlyY-pVWA";
    private static final String ID = "8YHvK0okEqXqa128F5r_L6_Op55dD5hiJgAI3Ofz0tgNow";

    @Test
    void test1() throws JsonProcessingException {
        ApiResponse<RiotAccountDto> response = riotApiService.getAccountByRiotId("hide on bush");
        assertNotNull(response);
    }

//    @Test
//    void test2() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String response = riotApiService.getSummonersByPuuid(PUUID);
//        RiotSummonerDto actual = objectMapper.readValue(response, RiotSummonerDto.class);
//        assertEquals(PUUID,actual.getPuuid());
//    }

//    @Test
//    void test3() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String response = riotApiService.getLeagueByid(ID);
//        List<RiotLeagueDto> actual = objectMapper.readValue(response, new TypeReference<>() {});
//        assertFalse(actual.isEmpty(), "리스트가 비어 있으면 안 됩니다");
//    }


    @Test
    void test5() throws JsonProcessingException {
        ApiResponse<List<RiotChampionMasteryDto>> actual = riotApiService.getChampionMasteryByPuuid(PUUID);
        actual.getData().sort(Comparator.comparingInt(RiotChampionMasteryDto::getChampionPoints).reversed());
        assertFalse(actual.getData().isEmpty(), "리스트가 비어 있으면 안 됩니다");
    }
}
