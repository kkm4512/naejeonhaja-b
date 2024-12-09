package com.example.naejeonhajab.domain.game.lol.service;

import com.example.naejeonhajab.domain.game.lol.dto.etc.LolPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.etc.LolTeamDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestPayloadDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.common.LolResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.security.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LolService {
    // 재생성
    LolResponseDto createTeam(List<LolRequestDto> lolRequestDtos);
    // 로그인 유저 팀 생성
    LolResponseDto createTeam(LolRequestPayloadDto lolRequestDtos, AuthUser authUser);
    // 비로그인 유저 팀 생성
    LolResponseDto createTeam(LolRequestPayloadDto lolRequestDtos);
    LolTeamDto splitTeam(List<LolPlayerDto> players);
    LolTeamDto generateBalanceByTier(LolTeamDto team);
    RiftPlayerHistoryResponseDetailDto getDetailTeam(Long playerHistoryId);
    Page<RiftPlayerHistoryResponseSimpleDto> getSimpleTeam(AuthUser authUser, Pageable pageable);
}
