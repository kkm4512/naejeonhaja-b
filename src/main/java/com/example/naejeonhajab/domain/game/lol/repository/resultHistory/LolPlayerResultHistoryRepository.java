package com.example.naejeonhajab.domain.game.lol.repository.resultHistory;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LolPlayerResultHistoryRepository extends JpaRepository<LolPlayerResultHistory, Long> {
    Page<LolPlayerResultHistory> findByUserAndType(User user, LolType lolType, Pageable pageable);
    @Query("SELECT lprh from LolPlayerResultHistory lprh" +
            " WHERE lprh.type = :lolType" +
            " AND lprh.playerResultHistoryTitle LIKE %:playerResultHistoryTitle%"
    )
    Page<LolPlayerResultHistory> searchPlayerResultHistoryByTitle(LolType lolType, Pageable pageable, String playerResultHistoryTitle);
}
