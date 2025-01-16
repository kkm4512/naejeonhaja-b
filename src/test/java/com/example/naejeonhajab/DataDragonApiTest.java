package com.example.naejeonhajab;

import com.example.naejeonhajab.domain.game.dataDragon.service.DataDragonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("dev") // dev 프로파일 활성화
class DataDragonApiTest {

    @Autowired
    DataDragonService dataDragonService;

    @Test
    void test1() throws JsonProcessingException {
        System.out.println(dataDragonService.getLatestVersion());
    }


}
