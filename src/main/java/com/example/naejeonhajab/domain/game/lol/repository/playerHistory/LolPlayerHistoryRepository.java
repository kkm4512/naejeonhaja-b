package com.example.naejeonhajab.domain.game.lol.repository.playerHistory;

import com.example.naejeonhajab.domain.game.lol.entity.playerHistory.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.enums.LolType;
import com.example.naejeonhajab.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LolPlayerHistoryRepository extends JpaRepository<LolPlayerHistory, Long> {
    Page<LolPlayerHistory> findByUserAndType(User user, LolType lolType, Pageable pageable);
    Optional<LolPlayerHistory> findByUserAndType(User user, LolType lolType);
    @Query("SELECT lph from LolPlayerHistory lph" +
            " WHERE lph.type = :lolType" +
            " AND lph.playerHistoryTitle LIKE %:playerHistoryTitle%"
    )
    Page<LolPlayerHistory> searchPlayerHistoryByTitle(User user, LolType lolType, Pageable pageable, String playerHistoryTitle);

}
