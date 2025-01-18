package com.example.naejeonhajab.domain.game.dataDragon.schedule;

import com.example.naejeonhajab.domain.game.dataDragon.service.DataDragonService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 배포환경에서의 챔피언 정보 미적용 문제
@Component
@RequiredArgsConstructor
public class GetLatestChampionsSchedule {
    private final DataDragonService dataDragonService;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * ?")
    public void getLatestChampions() {
        String version = dataDragonService.getLatestVersion();
        dataDragonService.initChampionRedis(version);
    }
}

