package com.example.naejeonhajab.domain.game.lol.repository.playerHistory;

import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LolPlayerRepository extends JpaRepository<LolPlayer, Long> {
    List<LolPlayer> findAllByPlayerHistory(LolPlayerHistory playerHistory);
}
