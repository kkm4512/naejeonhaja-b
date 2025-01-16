package com.example.naejeonhajab.domain.game.riot.service.redis;

import com.example.naejeonhajab.domain.game.riot.dto.RiotPlayerBasicDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotPlayerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RiotRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_NAME_RIOT_PLAYER = "riotPlayer::";
    private static final String REDIS_NAME_RIOT_BASIC_PLAYER = "riotPlayerBasic::";
    private static final String REDIS_KEY_PLAYER_NAME = "playerName:";

    public void setRiotPlayerDto(String key, RiotPlayerDto value) {
        redisTemplate.opsForValue().set(
                REDIS_NAME_RIOT_PLAYER +
                        REDIS_KEY_PLAYER_NAME +
                        key,
                value,
                Duration.ofHours(24)
        );
    }

    public void setRiotPlayerBasicDto(String key, RiotPlayerBasicDto value) {
        redisTemplate.opsForValue().set(
                REDIS_NAME_RIOT_BASIC_PLAYER +
                        REDIS_KEY_PLAYER_NAME +
                        key,
                value,
                Duration.ofHours(24)
        );
    }

    public boolean existsByPlayerName(String key) {
        return redisTemplate.opsForValue().get(
                REDIS_NAME_RIOT_PLAYER +
                        REDIS_KEY_PLAYER_NAME +
                        key
        ) != null;
    }

    public boolean existsByPlayerBasicName(String key) {
        return redisTemplate.opsForValue().get(
                REDIS_NAME_RIOT_BASIC_PLAYER +
                        REDIS_KEY_PLAYER_NAME +
                        key
        ) != null;
    }

    public RiotPlayerDto getRiotPlayerDto(String key) {
        return (RiotPlayerDto) redisTemplate.opsForValue().get(
                REDIS_NAME_RIOT_PLAYER +
                        REDIS_KEY_PLAYER_NAME +
                        key
        );
    }

    public RiotPlayerBasicDto getRiotPlayerBasicDto(String key) {
        return (RiotPlayerBasicDto) redisTemplate.opsForValue().get(
                REDIS_NAME_RIOT_BASIC_PLAYER +
                        REDIS_KEY_PLAYER_NAME +
                        key
        );
    }
}
