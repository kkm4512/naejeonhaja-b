package com.example.naejeonhajab.domain.game.lol.service.util;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.LolTeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LolUtilService {

    // 팀을 랜덤하게 5명으로 나눔
    public LolTeamResponseDto splitTeam(List<LolPlayerDto> riftPlayerRequestDtos) {
        List<LolPlayerDto> teamA = new ArrayList<>();
        List<LolPlayerDto> teamB = new ArrayList<>();
        try {
            Collections.shuffle(riftPlayerRequestDtos);
            for ( int i=0; i<riftPlayerRequestDtos.size()/2; i++ ){
                teamA.add(riftPlayerRequestDtos.get(i));
                teamB.add(riftPlayerRequestDtos.get(i + (riftPlayerRequestDtos.size()/2)));
            }
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }

        return new LolTeamResponseDto(teamA, teamB);
    }

    // 각 팀의 mmr 총합 계산
    public int calculateScore(List<LolPlayerDto> team) {
        return team.stream()
                .mapToInt(LolPlayerDto::getMmr)
                .sum();
    }

    // 10명의 유저에 대해 티어에 따른 MMR 부여 메서드
    public void initMmr(List<LolPlayerDto> riftRequestDtos){
        for (LolPlayerDto riftPlayerRequestDto : riftRequestDtos) {
            riftPlayerRequestDto.updateMmr(riftPlayerRequestDto.getTier().getScore());
        }
    }
}
