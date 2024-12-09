package com.example.naejeonhajab.domain.game.lol.repository;

import com.example.naejeonhajab.domain.game.lol.entity.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.LolPlayerLines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LolPlayerLinesRepository extends JpaRepository<LolPlayerLines, Long> {
    List<LolPlayerLines> findAllByPlayer(LolPlayer player);
}
