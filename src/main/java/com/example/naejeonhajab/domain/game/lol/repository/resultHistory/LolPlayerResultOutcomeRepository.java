package com.example.naejeonhajab.domain.game.lol.repository.resultHistory;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultOutcome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolPlayerResultOutcomeRepository extends JpaRepository<LolPlayerResultOutcome, Long> {
}