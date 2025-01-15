package com.example.naejeonhajab.aop;

import com.example.naejeonhajab.domain.game.lol.service.redis.LolRedisService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class GetLolPlayerResultStoreAspect {

    private final LolRedisService lolRedisService;

    @Pointcut("@annotation(com.example.naejeonhajab.annotation.GetLolPlayerResultHistoryStore)")
    private void getLolPlayerResultHistoryStoreAnnotation() {}

    @Around("getLolPlayerResultHistoryStoreAnnotation()")
    public Object getRiotPlayerStore(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Long playerResultHistoryId = (Long) args[0];  // 첫 번째 인자 추출

        if (lolRedisService.existPlayerResultHistoryDto(playerResultHistoryId) ) {
            return lolRedisService.getPlayerResultHistoryDto(playerResultHistoryId);
        }

        // ✅ 조건이 모두 실패했을 경우, 원래 메서드 실행
        return pjp.proceed();
    }
}
