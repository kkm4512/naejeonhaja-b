package com.example.naejeonhajab.aop;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotPlayerBasicDto;
import com.example.naejeonhajab.domain.game.riot.dto.RiotPlayerDto;
import com.example.naejeonhajab.domain.game.riot.service.redis.RiotRedisService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_PLAYER_FOUND;

@Aspect
@Component
@RequiredArgsConstructor
public class GetRiotPlayerStoreAspect {

    private final RiotRedisService riotRedisService;

    @Pointcut("@annotation(com.example.naejeonhajab.annotation.GetRiotPlayerStore)")
    private void getRiotPlayerStoreAnnotation() {}

    @Around("getRiotPlayerStoreAnnotation()")
    public Object getRiotPlayerStore(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        String playerName = (String) args[0];  // 첫 번째 인자 추출
        // ✅ 런타임에서 메서드의 반환 타입을 체크

        if (riotRedisService.existsByPlayerName(playerName)) {
            RiotPlayerDto riotPlayerDto = riotRedisService.getRiotPlayerDto(playerName);
            return ApiResponse.of(LOL_PLAYER_FOUND, riotPlayerDto);
        }

        // ✅ 조건이 모두 실패했을 경우, 원래 메서드 실행
        return pjp.proceed();
    }
}
