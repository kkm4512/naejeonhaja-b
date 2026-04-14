package com.example.naejeonhaja.domain.game.lol.enums;

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
    MASTER_I,
    GRANDMASTER_I,
    CHALLENGER_I;

    private static final EnumMap<LolTier, Integer> tierScoreMap = new EnumMap<>(LolTier.class);

    static {
        int baseScore = 50;
        int currentScore = 0;
        for (LolTier tier : LolTier.values()) {
            if (tier == UNRANKED)         currentScore = 0;
            else if (tier == MASTER_I)    currentScore += 200;
            else if (tier == GRANDMASTER_I) currentScore += 500;
            else if (tier == CHALLENGER_I)  currentScore += 1000;
            else                          currentScore += baseScore;
            tierScoreMap.put(tier, currentScore);
        }
    }

    public Integer getScore() {
        return tierScoreMap.getOrDefault(this, 0);
    }
}
