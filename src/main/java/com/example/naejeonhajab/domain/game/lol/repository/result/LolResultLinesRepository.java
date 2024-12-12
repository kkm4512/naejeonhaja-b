package com.example.naejeonhajab.domain.game.lol.repository.result;

import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolResultLines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LolResultLinesRepository extends JpaRepository<LolResultLines, Long> {
    List<LolResultLines> findAllByPlayerResult(LolPlayerResult playerResult);
}
