package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.common.LolRequestPayloadDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.rift.RiftRequestPayloadDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.rift.RiftResponseDto;
import com.example.naejeonhajab.domain.game.lol.service.LolService;
import com.example.naejeonhajab.security.AuthUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game/lol/rift")
public class RiftController {

    private final LolService lolService;

    public RiftController(@Qualifier("riftServiceImpl") LolService lolService) {
        this.lolService = lolService;
    }

    @PostMapping
    public ApiResponse<RiftResponseDto> createTeam(@RequestBody RiftRequestPayloadDto riftRequestPayloadDto, @AuthenticationPrincipal AuthUser authUser) {
        if (authUser != null) {
            RiftResponseDto result = (RiftResponseDto) lolService.createTeam(riftRequestPayloadDto, authUser);
            return ApiResponse.of(BaseApiResponse.SUCCESS, result);
        } else {
            RiftResponseDto result = (RiftResponseDto) lolService.createTeam(riftRequestPayloadDto);
            return ApiResponse.of(BaseApiResponse.SUCCESS, result);
        }
    }

    @PostMapping("/reCreate")
    public ApiResponse<RiftResponseDto> reCreateTeam(@RequestBody List<RiftRequestDto> riftRequestDtos) {
        List<LolRequestDto> lolRequestDtos = riftRequestDtos.stream().map(i -> (LolRequestDto) i).toList();
        RiftResponseDto result = (RiftResponseDto) lolService.createTeam(lolRequestDtos);
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
