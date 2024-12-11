package com.example.naejeonhajab.domain.game.lol.repository;

import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolLines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LolLinesRepository extends JpaRepository<LolLines, Long> {
    List<LolLines> findAllByPlayer(LolPlayer player);
}
