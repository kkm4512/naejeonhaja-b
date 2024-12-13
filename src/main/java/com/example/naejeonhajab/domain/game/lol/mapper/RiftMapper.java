package com.example.naejeonhajab.domain.game.lol.mapper;

import com.example.naejeonhajab.domain.game.lol.entity.player.LolLines;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayer;
import com.example.naejeonhajab.domain.game.lol.entity.player.LolPlayerHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RiftMapper {
    void insertPlayerHistory(LolPlayerHistory history);
    void insertPlayers(List<LolPlayer> players);
    void insertLines(List<LolLines> lines);
}


