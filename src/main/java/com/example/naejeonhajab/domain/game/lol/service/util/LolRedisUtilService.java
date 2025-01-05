package com.example.naejeonhajab.domain.game.lol.service.util;

import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LolRedisUtilService {

    // Redis
    private final RedisTemplate<String, Object> redisTemplate;

    // Constant
    private static final String REDIS_NAME_PLAYER_HISTORY = "playerhistory::";
    private static final String REDIS_KEY_PLAYER_HISTORY_ID = "playerhistoryId:";
    private static final String REDIS_KEY_USER_ID = "userId:";
    private static final String REDIS_KEY_PAGE = "page:";
    private static final String REDIS_NAME_PLAYER_RESULT_HISTORY = "playerResultHistory::";

    public Optional<LolPlayerHistoryResponseDetailDto>  getPlayerHistoryDetailTeam(Long playerHistoryId) {
        // Redis에서 데이터 가져오기
        return Optional.ofNullable((LolPlayerHistoryResponseDetailDto) redisTemplate.opsForValue().get(REDIS_NAME_PLAYER_HISTORY + playerHistoryId));
    }


    public void setPlayerHistoryDetailTeam(LolPlayerHistory lolPlayerHistory){
        redisTemplate.opsForValue().set(REDIS_NAME_PLAYER_HISTORY + lolPlayerHistory.getId(), LolPlayerHistoryResponseDetailDto.of(lolPlayerHistory));
    }

    public Optional<LolPlayerResultHistoryResponseDetailDto> getPlayerResultHistoryDetailTeam(Long playerResultHistoryId) {
        // Redis에서 데이터 가져오기
        LolPlayerResultHistoryResponseDetailDto cachedData = (LolPlayerResultHistoryResponseDetailDto) redisTemplate.opsForValue()
                .get(REDIS_NAME_PLAYER_RESULT_HISTORY + playerResultHistoryId);

        // Optional로 반환
        return Optional.ofNullable(cachedData);
    }


    public void setPlayerResultHistoryDetailTeam(LolPlayerResultHistory lolPlayerResultHistory){
        redisTemplate.opsForValue().set(REDIS_NAME_PLAYER_RESULT_HISTORY + lolPlayerResultHistory.getId(), LolPlayerResultHistoryResponseDetailDto.of(lolPlayerResultHistory));
    }


}
