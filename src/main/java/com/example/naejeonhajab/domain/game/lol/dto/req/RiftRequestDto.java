package com.example.naejeonhajab.domain.game.lol.dto.req;

import com.example.naejeonhajab.domain.game.lol.dto.etc.Lines;
import com.example.naejeonhajab.domain.game.lol.dto.etc.Player;
import com.example.naejeonhajab.domain.game.lol.enums.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RiftRequestDto {
    String name;
    Tier tier;
    List<Lines> lines;

    // 클라이언트로 부터 요청 데이터 -> 비즈니스 로직의 Player로 옮김
    public static List<Player> of(List<RiftRequestDto> riftRequestDtos) {
        HashMap<Tier,Integer> scoreMap = new HashMap<>();
        int baseScore = 50;
        int currentScore = 0; // UNRANKED는 0점부터 시작

        for (Tier tier : Tier.values()) {

            // 다음 티어로 넘어가는 점수 증가 조건
            switch (tier) {
                case UNRANKED -> currentScore += 0; // UNRANKED는 0점
                case IRON_I, BRONZE_I, SILVER_I, GOLD_I, PLATINUM_I, EMERALD_I, DIAMOND_I -> currentScore += 100; // 티어가 바뀌는 경우
                case MASTER -> currentScore += 200; // DIAMOND -> MASTER
                case GRANDMASTER -> currentScore += 300; // MASTER -> GRANDMASTER
                case CHALLENGER -> currentScore += 500; // GRANDMASTER -> CHALLENGER
                default -> currentScore += baseScore; // 기본적으로 50점씩 증가
            }
            scoreMap.put(tier, currentScore);
        }

        return riftRequestDtos.stream()
                .map(dto -> new Player(dto.getName(), dto.getTier(), dto.getLines(), scoreMap.get(dto.getTier())))
                .collect(Collectors.toList());
    }

}
