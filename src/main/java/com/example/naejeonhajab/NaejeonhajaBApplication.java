package com.example.naejeonhajab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.naejeonhajab.domain.game.lol.mapper")
public class NaejeonhajaBApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaejeonhajaBApplication.class, args);
    }

}
