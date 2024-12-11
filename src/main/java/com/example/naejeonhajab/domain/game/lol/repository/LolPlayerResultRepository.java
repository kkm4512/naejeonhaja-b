package com.example.naejeonhajab.domain.game.lol.repository;

import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolPlayerResultRepository extends JpaRepository<LolPlayerResult, Long> {
}
