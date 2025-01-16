package com.example.naejeonhajab.domain.game.lol.service.redis;

import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistoryDto;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LolRedisService {

    // Redis
    private final RedisTemplate<String, Object> redisTemplate;

    // Constant
    private static final String REDIS_NAME_PLAYER_HISTORY = "playerhistory::";
    private static final String REDIS_KEY_PLAYER_HISTORY_ID = "playerHistoryId:";
    private static final String REDIS_KEY_PLAYER_RESULT_HISTORY_ID = "playerResultHistoryId:";
    private static final String REDIS_NAME_PLAYER_RESULT_HISTORY = "playerResultHistory::";


    // PlayerHistory
    public LolPlayerHistoryDto getPlayerHistoryDto(Long playerHistoryId) {
        // Redis에서 데이터 가져오기
        return (LolPlayerHistoryDto) redisTemplate.opsForValue().get(
                REDIS_NAME_PLAYER_HISTORY +
                        REDIS_KEY_PLAYER_HISTORY_ID +
                        playerHistoryId
        );
    }

    // PlayerHistory
    public boolean existPlayerHistoryDto(Long playerHistoryId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(
                REDIS_NAME_PLAYER_HISTORY +
                        REDIS_KEY_PLAYER_HISTORY_ID +
                        playerHistoryId
        ));
    }


    public void setPlayerHistoryDto(LolPlayerHistory lolPlayerHistory){
        redisTemplate.opsForValue().set(
                REDIS_NAME_PLAYER_HISTORY + REDIS_KEY_PLAYER_HISTORY_ID + lolPlayerHistory.getId(),
                LolPlayerHistoryDto.of(lolPlayerHistory),
                Duration.ofHours(24)
        );
    }

    public void deletePlayerHistoryDto(LolPlayerHistory lolPlayerHistory){
        boolean flag = existPlayerHistoryDto(lolPlayerHistory.getId());
        if (flag){
            redisTemplate.delete(
                    REDIS_NAME_PLAYER_HISTORY +
                            REDIS_KEY_PLAYER_HISTORY_ID +
                            lolPlayerHistory.getId()
            );
        }
    }

    public void updatePlayerHistoryDto(LolPlayerHistory lolPlayerHistory){
        boolean flag = existPlayerHistoryDto(lolPlayerHistory.getId());
        if (flag){
            redisTemplate.opsForValue().set(
                    REDIS_NAME_PLAYER_HISTORY + REDIS_KEY_PLAYER_HISTORY_ID + lolPlayerHistory.getId(),
                    LolPlayerHistoryDto.of(lolPlayerHistory),
                    Duration.ofHours(24)
            );
        }
    }

    // PlayerResultHistory
    public LolPlayerResultHistoryDto getPlayerResultHistoryDto(Long playerResultHistoryId) {
        // Redis에서 데이터 가져오기
        // Redis에서 데이터 가져오기
        return (LolPlayerResultHistoryDto) redisTemplate.opsForValue()
                .get(
                        REDIS_NAME_PLAYER_RESULT_HISTORY +
                                REDIS_KEY_PLAYER_RESULT_HISTORY_ID +
                                playerResultHistoryId
                );
    }

    // PlayerHistory
    public boolean existPlayerResultHistoryDto(Long playerResultHistoryId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(
                REDIS_NAME_PLAYER_RESULT_HISTORY +
                        REDIS_KEY_PLAYER_RESULT_HISTORY_ID +
                        playerResultHistoryId
        ));

    }


    public void setPlayerResultHistoryDto(LolPlayerResultHistory lolPlayerResultHistory){
        redisTemplate.opsForValue().set(
                REDIS_NAME_PLAYER_RESULT_HISTORY + REDIS_KEY_PLAYER_RESULT_HISTORY_ID + lolPlayerResultHistory.getId(),
                LolPlayerResultHistoryDto.of(lolPlayerResultHistory),
                Duration.ofHours(24)
        );
    }

    public void deletePlayerResultHistoryDto(LolPlayerResultHistory lolPlayerResultHistory){
        boolean flag = existPlayerResultHistoryDto(lolPlayerResultHistory.getId());
        if (flag){
            redisTemplate.delete(
                    REDIS_NAME_PLAYER_RESULT_HISTORY +
                            REDIS_KEY_PLAYER_RESULT_HISTORY_ID +
                            lolPlayerResultHistory.getId()
            );
        }
    }

    public void updatePlayerResultHistoryDto(LolPlayerResultHistory lolPlayerResultHistory){
        boolean flag = existPlayerResultHistoryDto(lolPlayerResultHistory.getId());
        if (flag){
            redisTemplate.opsForValue().set(
                    REDIS_NAME_PLAYER_RESULT_HISTORY + REDIS_KEY_PLAYER_RESULT_HISTORY_ID + lolPlayerResultHistory.getId(),
                    LolPlayerResultHistoryDto.of(lolPlayerResultHistory),
                    Duration.ofHours(24)
            );
        }
    }


}
