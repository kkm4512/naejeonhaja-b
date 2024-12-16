package com.example.naejeonhajab.domain.game.lol.mapper;

import com.example.naejeonhajab.domain.game.lol.entity.player.LolLines;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResult;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultHistory;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolPlayerResultOutcome;
import com.example.naejeonhajab.domain.game.lol.entity.result.LolResultLines;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LolMapper {
    // PlayerHistory
    void insertPlayerHistory(LolPlayerHistory history);
    void insertPlayers(List<LolPlayer> players);
    void insertLines(List<LolLines> lines);

    // ReulstHistory
    void inserPlayerResultHistory(LolPlayerResultHistory history);
    void insertPlayerResultOutcome(List<LolPlayerResultOutcome> outcomes);
    void insertPlayersResult(List<LolPlayerResult> players);
    void insertResultLines(List<LolResultLines> lines);

}


