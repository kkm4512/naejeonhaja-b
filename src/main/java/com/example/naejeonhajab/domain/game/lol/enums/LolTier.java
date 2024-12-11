package com.example.naejeonhajab.domain.game.lol.enums;

import java.util.EnumMap;

public enum LolTier {
    UNRANKED,
    IRON_IV, IRON_III, IRON_II, IRON_I,
    BRONZE_IV, BRONZE_III, BRONZE_II, BRONZE_I,
    SILVER_IV, SILVER_III, SILVER_II, SILVER_I,
    GOLD_IV, GOLD_III, GOLD_II, GOLD_I,
    PLATINUM_IV, PLATINUM_III, PLATINUM_II, PLATINUM_I,
    EMERALD_IV, EMERALD_III, EMERALD_II, EMERALD_I,
    DIAMOND_IV, DIAMOND_III, DIAMOND_II, DIAMOND_I,
    MASTER,
    GRANDMASTER,
    CHALLENGER;

    // 점수 맵 정의
    private static final EnumMap<LolTier, Integer> tierScoreMap = new EnumMap<>(LolTier.class);

    static {
        int baseScore = 50;
        int currentScore = 0; // UNRANKED는 0점부터 시작

        for (LolTier tier : LolTier.values()) {
            switch (tier) {
                case UNRANKED -> currentScore += 0; // UNRANKED는 0점
                case IRON_I, BRONZE_I, SILVER_I, GOLD_I, PLATINUM_I, EMERALD_I, DIAMOND_I ->
                        currentScore += 100; // 주요 티어 전환 시 점수 증가
                case MASTER -> currentScore += 200; // DIAMOND -> MASTER
                case GRANDMASTER -> currentScore += 500; // MASTER -> GRANDMASTER
                case CHALLENGER -> currentScore += 1000; // GRANDMASTER -> CHALLENGER
                default -> currentScore += baseScore; // 기본적으로 50점씩 증가
            }
            tierScoreMap.put(tier, currentScore); // 각 티어에 점수 매핑
        }
    }

    // 현재 티어의 점수를 반환하는 메서드
    public Integer getScore() {
        return tierScoreMap.getOrDefault(this, 0);
    }
}
