package com.example.naejeonhajab.domain.game.riot.service.util;

import org.springframework.stereotype.Service;

@Service
public class RiotUtilService {

    public String[] splitByShop(String playerName){
        String[] parts = new String[2];
        // # 기준으로 나누기
        if (playerName.contains("#")) {
            String[] split = playerName.split("#", 2); // 최대 2개로 분할
            parts[0] = split[0];
            parts[1] = split[1];
        } else {
            // #이 없는 경우, tagLine을 기본값으로 설정
            parts[0] = playerName;
            parts[1] = "kr1";
        }
        return parts;
    }

}
