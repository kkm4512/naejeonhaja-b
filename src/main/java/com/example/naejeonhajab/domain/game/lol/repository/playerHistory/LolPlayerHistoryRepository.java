package com.example.naejeonhajab.domain.game.lol.repository.playerHistory;

import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LolPlayerHistoryRepository extends JpaRepository<LolPlayerHistory, Long> {
    Page<LolPlayerHistory> findByUserAndType(User user, LolType lolType, Pageable pageable);

}
