package com.example.naejeonhajab.domain.game.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotChampionMasteryDto {
    private String puuid;
    private int championId;
    private int championLevel;
    private int championPoints;
    private long lastPlayTime;
    private int championPointsSinceLastLevel;
    private int championPointsUntilNextLevel;
    private int markRequiredForNextLevel;
    private int tokensEarned;
    private int championSeasonMilestone;
    private List<String> milestoneGrades;
    private NextSeasonMilestoneDto nextSeasonMilestone;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NextSeasonMilestoneDto {
        private Map<String, Integer> requireGradeCounts;
        private int rewardMarks;
        private boolean bonus;
        private RewardConfigDto rewardConfig;
        private int totalGamesRequires;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RewardConfigDto {
            private String rewardValue;
            private String rewardType;
            private int maximumReward;
        }
    }
}
