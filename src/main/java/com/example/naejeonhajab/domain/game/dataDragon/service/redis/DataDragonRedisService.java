package com.example.naejeonhajab.domain.game.dataDragon.service.redis;

import com.example.naejeonhajab.domain.game.dataDragon.dto.DataDragonChampionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataDragonRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_NAME_DATA_DRAGON_CHAMPION = "dataDragonChampion::";
    private static final String REDIS_KEY_CHAMPION_ID = "championId:";

    public void setChampionDto(String key, DataDragonChampionDto.ChampionDto value) {
        redisTemplate.opsForValue().set(
                REDIS_NAME_DATA_DRAGON_CHAMPION +
                        REDIS_KEY_CHAMPION_ID + key,
                value
        );
    }

    public DataDragonChampionDto.ChampionDto getChampionDto(String key) {
        return (DataDragonChampionDto.ChampionDto) redisTemplate.opsForValue().get(
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
