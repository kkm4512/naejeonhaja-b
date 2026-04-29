package com.example.naejeonhaja.domain.game.lol.enums;

public enum LolBalanceEnum {
    // 팀 A와 팀 B의 MMR 합계 차이가 0~100: 거의 완벽한 밸런스
    PERFECT(0, 100, "완벽한 밸런스"),
    // 팀 A와 팀 B의 MMR 합계 차이가 101~300: 매우 좋은 밸런스
    EXCELLENT(101, 300, "우수한 밸런스"),
    // 팀 A와 팀 B의 MMR 합계 차이가 301~600: 괜찮은 밸런스
    GOOD(301, 600, "좋은 밸런스"),
    // 팀 A와 팀 B의 MMR 합계 차이가 601~1000: 보통 정도의 밸런스
    FAIR(601, 1000, "보통 밸런스"),
    // 팀 A와 팀 B의 MMR 합계 차이가 1001 이상: 심각한 불균형
    POOR(1001, Integer.MAX_VALUE, "불균형한 밸런스");

    private final int minDiff;
    private final int maxDiff;
    private final String description;

    LolBalanceEnum(int minDiff, int maxDiff, String description) {
        this.minDiff = minDiff;
        this.maxDiff = maxDiff;
        this.description = description;
    }

    public int getMinDiff() {
        return minDiff;
    }

    public int getMaxDiff() {
        return maxDiff;
    }

    public String getDescription() {
        return description;
    }

    public static LolBalanceEnum fromDifference(int diff) {
        for (LolBalanceEnum balance : values()) {
            if (diff >= balance.minDiff && diff <= balance.maxDiff) {
                return balance;
            }
        }
        return POOR;
    }
}
