package com.example.naejeonhajab.domain.game.lol.repository.player;

import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import com.example.naejeonhajab.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolPlayerHistoryRepository extends JpaRepository<LolPlayerHistory, Long> {
    Page<LolPlayerHistory> findByUser(User user, Pageable pageable);
}
