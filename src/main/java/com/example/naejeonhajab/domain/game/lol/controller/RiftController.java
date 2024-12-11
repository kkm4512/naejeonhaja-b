package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.player.RiftPlayerRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.result.RiftPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.player.RiftPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.player.RiftPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.player.RiftTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.result.RiftPlayerResultHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.result.RiftPlayerResultHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.service.RiftServiceImpl;
import com.example.naejeonhajab.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game/lol/rift")
public class RiftController {

    private final RiftServiceImpl lolService;

    @PostMapping
    public ApiResponse<RiftTeamResponseDto> createTeam(@RequestBody @Valid List<RiftPlayerRequestDto> riftPlayerRequestDtos) {
        for (RiftPlayerRequestDto riftPlayerRequestDto : riftPlayerRequestDtos) {
            riftPlayerRequestDto.updateMmr(riftPlayerRequestDto.getTier().getScore());
        }
        RiftTeamResponseDto result = lolService.createTeam(riftPlayerRequestDtos);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @PostMapping("/playerHistory")
    public ApiResponse<RiftTeamResponseDto> createPlayerHistoryAndTeam(@RequestBody @Valid RiftPlayerHistoryRequestDto riftRequestPayloadDto, @AuthenticationPrincipal AuthUser authUser) {
        RiftTeamResponseDto result = lolService.createPlayerHistoryAndTeam(riftRequestPayloadDto, authUser);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @PostMapping("/playerResultHistory")
    public ApiResponse<Void> createResultTeam(@RequestBody @Valid RiftPlayerResultHistoryRequestDto riftPlayerResultHistoryRequestDto, @AuthenticationPrincipal AuthUser authUser) {
        lolService.createResultTeam(riftPlayerResultHistoryRequestDto,authUser);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
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

    // title, 10명의 유저 정보
    @GetMapping("/playerResultHistory/detail/{playerResultHistoryId}")
    public ApiResponse<RiftPlayerResultHistoryResponseDetailDto> getDetailResultTeam(@PathVariable Long playerResultHistoryId){
        RiftPlayerResultHistoryResponseDetailDto result = lolService.getDetailResultTeam(playerResultHistoryId);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    // id, title만
    @GetMapping("/playerResultHistory/simple/{page}")
    public ApiResponse<Page<RiftPlayerResultHistoryResponseSimpleDto>> getSimpleResultTeam(@PathVariable int page, @AuthenticationPrincipal AuthUser authUser){
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC,"createdAt");
        Page<RiftPlayerResultHistoryResponseSimpleDto> result = lolService.getSimpleResultTeam(authUser,pageable);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
