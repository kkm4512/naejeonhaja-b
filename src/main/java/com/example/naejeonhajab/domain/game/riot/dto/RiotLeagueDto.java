package com.example.naejeonhajab.domain.game.riot.dto;

import com.example.naejeonhajab.domain.game.riot.enums.LolRankType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotLeagueDto {
    private String leagueId;
    private LolRankType queueType;
    private String tier;
    private String rank;
    private String summonerId;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean veteran;   // 리그에서 오래 활동한 플레이어인지 여부
    private boolean inactive;  // 계정이 비활성화 상태인지 여부
    private boolean freshBlood; // 신규 플레이어인지 여부
    private boolean hotStreak; // 현재 연승 중인지 여부
}
