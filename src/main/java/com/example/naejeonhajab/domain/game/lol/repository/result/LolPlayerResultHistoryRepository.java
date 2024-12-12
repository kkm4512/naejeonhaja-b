package com.example.naejeonhajab.domain.game.lol.repository.result;

import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolPlayerResultHistoryRepository extends JpaRepository<LolPlayerResultHistory, Long> {
    Page<LolPlayerResultHistory> findByUser(User user, Pageable pageable);
}
