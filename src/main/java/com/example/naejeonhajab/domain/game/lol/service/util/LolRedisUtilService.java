package com.example.naejeonhajab.domain.game.lol.service.util;

import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistoryDto;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LolRedisUtilService {

    // Redis
    private final RedisTemplate<String, Object> redisTemplate;

    // Constant
    private static final String REDIS_NAME_PLAYER_HISTORY = "playerhistory::";
    private static final String REDIS_KEY_PLAYER_HISTORY_ID = "playerHistoryId:";
    private static final String REDIS_USER_ID = "userId:";
    private static final String REDIS_KEY_PLAYER_RESULT_HISTORY_ID = "playerResultHistoryId:";
    private static final String REDIS_NAME_PLAYER_RESULT_HISTORY = "playerResultHistory::";


    // PlayerHistory
    public Optional<LolPlayerHistoryDto> getPlayerHistoryDto(Long playerHistoryId) {
        // Redis에서 데이터 가져오기
        return Optional.ofNullable((LolPlayerHistoryDto) redisTemplate.opsForValue().get(
                REDIS_NAME_PLAYER_HISTORY +
                        REDIS_KEY_PLAYER_HISTORY_ID +
                        playerHistoryId
                )
        );
    }

    public void setPlayerHistoryDto(LolPlayerHistory lolPlayerHistory){
        redisTemplate.opsForValue().set(
                REDIS_NAME_PLAYER_HISTORY + REDIS_KEY_PLAYER_HISTORY_ID + lolPlayerHistory.getId(),
                LolPlayerHistoryDto.of(lolPlayerHistory)
                ,Duration.ofMinutes(5)
        );
    }

    public void deletePlayerHistoryDto(LolPlayerHistory lolPlayerHistory){
        Object result = redisTemplate.opsForValue().get(
                REDIS_NAME_PLAYER_HISTORY +
                        REDIS_KEY_PLAYER_HISTORY_ID +
                        lolPlayerHistory.getId()
        );
        if (result != null){
            redisTemplate.delete(
                    REDIS_NAME_PLAYER_HISTORY +
                            REDIS_KEY_PLAYER_HISTORY_ID +
                            lolPlayerHistory.getId()
            );
        }
    }

    public void updatePlayerHistoryDto(LolPlayerHistory lolPlayerHistory){
        Object result = redisTemplate.opsForValue().get(
                REDIS_NAME_PLAYER_HISTORY +
                        REDIS_KEY_PLAYER_HISTORY_ID +
                        lolPlayerHistory.getId()
        );
        if (result != null){
            redisTemplate.opsForValue().set(
                    REDIS_NAME_PLAYER_HISTORY + REDIS_KEY_PLAYER_HISTORY_ID + lolPlayerHistory.getId(),
                    LolPlayerHistoryDto.of(lolPlayerHistory),
                    Duration.ofMinutes(5)
            );
        }
    }

    // PlayerResultHistory
    public Optional<LolPlayerResultHistoryDto> getPlayerResultHistoryDto(Long playerResultHistoryId) {
        // Redis에서 데이터 가져오기
        LolPlayerResultHistoryDto cachedData = (LolPlayerResultHistoryDto) redisTemplate.opsForValue()
                .get(
                        REDIS_NAME_PLAYER_RESULT_HISTORY +
                                REDIS_KEY_PLAYER_RESULT_HISTORY_ID +
                                playerResultHistoryId
                );

        // Optional로 반환
        return Optional.ofNullable(cachedData);
    }


    public void setPlayerResultHistoryDto(LolPlayerResultHistory lolPlayerResultHistory){
        redisTemplate.opsForValue().set(
                REDIS_NAME_PLAYER_RESULT_HISTORY + REDIS_KEY_PLAYER_RESULT_HISTORY_ID + lolPlayerResultHistory.getId(),
                LolPlayerResultHistoryDto.of(lolPlayerResultHistory)
                ,Duration.ofMinutes(5));
    }

    public void deletePlayerResultHistoryDto(LolPlayerResultHistory lolPlayerResultHistory){
        Object result = redisTemplate.opsForValue().get(
                REDIS_NAME_PLAYER_RESULT_HISTORY +
                        REDIS_KEY_PLAYER_RESULT_HISTORY_ID +
                        lolPlayerResultHistory.getId()
        );
        if (result != null){
            redisTemplate.delete(
                    REDIS_NAME_PLAYER_RESULT_HISTORY +
                            REDIS_KEY_PLAYER_RESULT_HISTORY_ID +
                            lolPlayerResultHistory.getId()
            );
        }
    }

    public void updatePlayerResultHistoryDto(LolPlayerResultHistory lolPlayerResultHistory){
        Object result = redisTemplate.opsForValue().get(
                REDIS_NAME_PLAYER_RESULT_HISTORY +
                        REDIS_KEY_PLAYER_RESULT_HISTORY_ID +
                        lolPlayerResultHistory.getId()
        );
        if (result != null){
            redisTemplate.opsForValue().set(
                    REDIS_NAME_PLAYER_RESULT_HISTORY + REDIS_KEY_PLAYER_RESULT_HISTORY_ID + lolPlayerResultHistory.getId(),
                    LolPlayerResultHistoryDto.of(lolPlayerResultHistory),
                    Duration.ofMinutes(5)
            );
        }
    }


}
