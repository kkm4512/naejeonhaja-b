package com.example.naejeonhajab.domain.game.lol.repository.resultHistory;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolPlayerResultRepository extends JpaRepository<LolPlayerResult, Long> {
}
