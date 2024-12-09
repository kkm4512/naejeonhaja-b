package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftPlayerRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.service.RiftServiceImpl;
import com.example.naejeonhajab.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game/lol/rift")
public class RiftController {

    private final RiftServiceImpl lolService;

    @PostMapping
    public ApiResponse<RiftTeamResponseDto> createTeam(@RequestBody List<RiftPlayerRequestDto> riftPlayerRequestDtos) {
        RiftTeamResponseDto result = lolService.createTeam(riftPlayerRequestDtos);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @PostMapping("/history")
    public ApiResponse<RiftTeamResponseDto> createPlayerHistoryAndTeam(@RequestBody RiftPlayerHistoryRequestDto riftRequestPayloadDto, @AuthenticationPrincipal AuthUser authUser) {
        RiftTeamResponseDto result = lolService.createPlayerHistoryAndTeam(riftRequestPayloadDto, authUser);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    // title, 10명의 유저 정보
    @GetMapping("/playerHistory/detail/{playerHistoryId}")
    public ApiResponse<RiftPlayerHistoryResponseDetailDto> getDetailTeam(@PathVariable Long playerHistoryId){
        RiftPlayerHistoryResponseDetailDto result = lolService.getDetailTeam(playerHistoryId);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }


    // id, title만
    @GetMapping("/playerHistory/simple/{page}")
    public ApiResponse<Page<RiftPlayerHistoryResponseSimpleDto>> getSimpleTeam(@PathVariable int page, @AuthenticationPrincipal AuthUser authUser){
        Pageable pageable = PageRequest.of(page - 1, 3, Sort.Direction.DESC,"createdAt");
        Page<RiftPlayerHistoryResponseSimpleDto> result = lolService.getSimpleTeam(authUser,pageable);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
