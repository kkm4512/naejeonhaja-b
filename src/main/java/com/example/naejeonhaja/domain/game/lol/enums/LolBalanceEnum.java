package com.example.naejeonhaja.domain.game.lol.enums;

public enum LolBalanceEnum {
    // 팀 A와 팀 B의 MMR 합계 차이가 100 이하일 때
    GOOD,
    // 팀 A와 팀 B의 MMR 합계 차이가 101 ~ 300일 때
    FAIR,
    // 팀 A와 팀 B의 MMR 합계 차이가 301 이상일 때
    BAD
}
