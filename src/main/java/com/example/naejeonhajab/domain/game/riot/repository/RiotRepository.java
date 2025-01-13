package com.example.naejeonhajab.domain.game.riot.repository;

import com.example.naejeonhajab.domain.game.riot.entity.RiotPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiotRepository extends JpaRepository<RiotPlayer,Long> {
    boolean existsByPlayerName(String playerName);
    RiotPlayer findByPlayerName(String playerName);
}
