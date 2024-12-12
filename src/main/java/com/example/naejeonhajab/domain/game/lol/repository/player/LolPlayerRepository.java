package com.example.naejeonhajab.domain.game.lol.repository.player;

import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LolPlayerRepository extends JpaRepository<LolPlayer, Long> {
    List<LolPlayer> findAllByPlayerHistory(LolPlayerHistory playerHistory);
}
