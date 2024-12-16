package com.example.naejeonhajab.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class TimeAspect {

    @Pointcut("@annotation(com.example.naejeonhajab.annotation.TrackingTime)")
    private void trackTimeAnnotation(){}

    @Around("trackTimeAnnotation()")
    public Object timeAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("AOP applied to method: {}", pjp.getSignature().toShortString());
        Long startTime = System.nanoTime();
        String methodName = pjp.getSignature().toShortString();
        try {
            return pjp.proceed();
        } catch (Exception e){
          throw e;
        } finally {
            Long endTime = System.nanoTime();
            log.debug("현재 {} 의 측정시간 : {}ms",methodName, (endTime - startTime) / 1_000_000.0);
        }
    }
}
