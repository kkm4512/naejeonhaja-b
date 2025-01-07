package com.example.naejeonhajab;

import com.example.naejeonhajab.domain.game.dataDragon.dto.DataDragonChampionDto;
import com.example.naejeonhajab.domain.game.dataDragon.service.DataDragonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("dev") // dev 프로파일 활성화
class DataDragonApiTest {

    @Autowired
    DataDragonService dataDragonService;


    @Test
    void test1() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
        DataDragonChampionDto.ChampionDto championByChampionId = dataDragonService.getChampionDtoByChampionId("1");
//        System.out.println(objectMapper.writeValueAsString(championByChampionId));
        assertNotNull(championByChampionId);
    }

}
