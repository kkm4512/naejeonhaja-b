package com.example.naejeonhaja.domain.game.riot.service.util;

import org.springframework.stereotype.Service;

@Service
public class RiotUtilService {

    public String[] splitByShop(String playerName) {
        if (playerName.contains("#")) {
            return playerName.split("#", 2);
        }
        return new String[]{playerName, "KR1"};
    }
}
