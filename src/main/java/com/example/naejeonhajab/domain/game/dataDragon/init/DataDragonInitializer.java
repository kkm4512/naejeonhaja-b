package com.example.naejeonhajab.domain.game.dataDragon.init;

import com.example.naejeonhajab.domain.game.dataDragon.service.DataDragonService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataDragonInitializer {
    private final DataDragonService dataDragonService;

    public DataDragonInitializer(DataDragonService dataDragonService) {
        this.dataDragonService = dataDragonService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeChampions() {
        dataDragonService.initChampionRedis();
    }
}
