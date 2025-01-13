package com.example.naejeonhajab.domain.game.dataDragon.service.redis;

import com.example.naejeonhajab.domain.game.dataDragon.dto.DataDragonChampionDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotChampionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataDragonRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_NAME_DATA_DRAGON_CHAMPION = "dataDragonChampion::";
    private static final String REDIS_KEY_CHAMPION_ID = "championId:";

    public void setChampionDto(String key, RiotChampionDto value) {
        redisTemplate.opsForValue().set(
                REDIS_NAME_DATA_DRAGON_CHAMPION +
                        REDIS_KEY_CHAMPION_ID + key,
                value
        );
    }

    public RiotChampionDto getChampionDto(String key) {
        return (RiotChampionDto) redisTemplate.opsForValue().get(
                REDIS_NAME_DATA_DRAGON_CHAMPION +
                        REDIS_KEY_CHAMPION_ID + key
        );
    }

    public boolean existsChampionDto() {
        Object response = redisTemplate.opsForValue().get(
                REDIS_NAME_DATA_DRAGON_CHAMPION +
                        REDIS_KEY_CHAMPION_ID + 1
        );
        return response != null;
    }
}
