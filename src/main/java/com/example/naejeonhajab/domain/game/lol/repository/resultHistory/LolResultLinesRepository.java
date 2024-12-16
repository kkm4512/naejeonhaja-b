package com.example.naejeonhajab.domain.game.lol.repository.resultHistory;

import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.resultHistory.LolResultLines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LolResultLinesRepository extends JpaRepository<LolResultLines, Long> {
    List<LolResultLines> findAllByPlayerResult(LolPlayerResult playerResult);
}
