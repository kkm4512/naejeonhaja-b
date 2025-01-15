package com.example.naejeonhajab.aop;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryDto;
import com.example.naejeonhajab.domain.game.lol.service.redis.LolRedisService;
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
public class GetLolPlayerStoreAspect {

    private final LolRedisService lolRedisService;

    @Pointcut("@annotation(com.example.naejeonhajab.annotation.GetLolPlayerHistoryStore)")
    private void getLolPlayerHistoryStoreAnnotation() {}

    @Around("getLolPlayerHistoryStoreAnnotation()")
    public Object getRiotPlayerStore(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Long playerHistoryId = (Long) args[0];  // 첫 번째 인자 추출

        if (lolRedisService.existPlayerHistoryDto(playerHistoryId) ) {
            return lolRedisService.getPlayerHistoryDto(playerHistoryId);
        }

        // ✅ 조건이 모두 실패했을 경우, 원래 메서드 실행
        return pjp.proceed();
    }
}
