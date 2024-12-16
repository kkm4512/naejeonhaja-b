package com.example.naejeonhajab.config;

import com.example.naejeonhajab.aop.TimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AspectConfig {
    @Bean
    public TimeAspect getAspect() {
        return new TimeAspect();
    }
}
