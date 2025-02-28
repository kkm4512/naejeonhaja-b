package com.example.naejeonhajab.aop;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.riot.dto.RiotPlayerBasicDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotPlayerDto;
import com.example.naejeonhajab.domain.game.riot.service.redis.RiotRedisService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_PLAYER_FOUND;

@Aspect
@Component
@RequiredArgsConstructor
public class GetRiotPlayerStoreBasicAspect {

    private final RiotRedisService riotRedisService;

    @Pointcut("@annotation(com.example.naejeonhajab.annotation.GetRiotPlayerBasicStore)")
    private void getRiotPlayerBasicStoreAnnotation() {}

    @Around("getRiotPlayerBasicStoreAnnotation()")
    public Object getRiotPlayerBasicStore(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        String playerName = (String) args[0];  // 첫 번째 인자 추출
        if (riotRedisService.existsByPlayerBasicName(playerName)) {
            RiotPlayerBasicDto riotPlayerBasicDto = riotRedisService.getRiotPlayerBasicDto(playerName);
            return ApiResponse.of(LOL_PLAYER_FOUND, riotPlayerBasicDto);
        }

        // ✅ 조건이 모두 실패했을 경우, 원래 메서드 실행
        return pjp.proceed();
    }
}
