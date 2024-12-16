package com.example.naejeonhajab.domain.game.lol.service.balancing;

import com.example.naejeonhajab.common.exception.BaseException;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolLinesDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.LolTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.enums.LolLine;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LolLineSortServiceImpl {
    // MainLine으로 정렬된 새로운 팀을 반환
    public LolTeamResponseDto normalizeLineOrder(LolTeamResponseDto team) {
        List<LolPlayerDto> teamA;
        List<LolPlayerDto> teamB;
        try {
            teamA = soryTeamByLineRole(team.getTeamA());
            teamB = soryTeamByLineRole(team.getTeamB());
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return new LolTeamResponseDto(teamA, teamB);
    }

    // MainLine은 앞으로, SubLine은 뒤로
    private List<LolPlayerDto> soryTeamByLineRole(List<LolPlayerDto> players){
        return players.stream()
                .map(player -> {
                    // lines를 MainLine이 먼저 오도록 정렬
                    List<LolLinesDto> reorderedLines = player.getLines().stream()
                            .sorted((line1, line2) -> {
                                if (line1.getLineRole().isMainRole()) return -1; // MainLine을 앞에 배치
                                if (line2.getLineRole().isMainRole()) return 1;
                                return 0; // 그 외는 순서를 유지
                            })
                            .toList();

                    // Player의 lines를 재배열 후 새로운 Player 객체 생성 (불변 객체일 경우)
                    return new LolPlayerDto(player.getName(), player.getTier(),reorderedLines,player.getMmr(),player.getMmrReduced());
                })
                .collect(Collectors.toList());
    }

    // 라인순서대로 정리 (전체적)
    public LolTeamResponseDto orderByLine(LolTeamResponseDto team) {
        List<LolPlayerDto> sortedTeamA;
        List<LolPlayerDto> sortedTeamB;
        try {
            // 원하는 라인 순서
            List<LolLine> lineOrder = List.of(LolLine.values());

            // Team A 정렬
            sortedTeamA = sortTeamByLine(team.getTeamA(), lineOrder);

            // Team B 정렬
            sortedTeamB = sortTeamByLine(team.getTeamB(), lineOrder);

            // 정렬된 팀으로 새로운 Team 객체 반환
        } catch (Exception e) {
            throw new BaseException(BaseApiResponse.TEAM_MISMATCH);
        }
        return new LolTeamResponseDto(sortedTeamA, sortedTeamB);
    }

    // 실제 라인 정렬 담당 함수
    private List<LolPlayerDto> sortTeamByLine(List<LolPlayerDto> riftPlayerRequestDtos, List<LolLine> lineOrder) {
        // Player를 정렬하는 로직
        return riftPlayerRequestDtos.stream()
                .sorted(Comparator.comparingInt(player -> {
                    // 각 Player의 라인을 기준으로 정렬 우선순위를 설정
                    LolLine playerLine = player.getLines().get(0).getLine(); // 첫 번째 라인을 가져옴 (MainLine이 할당되었음)
                    return lineOrder.indexOf(playerLine); // 라인 순서의 인덱스 반환
                }))
                .collect(Collectors.toList());
    }

}
