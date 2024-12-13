package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.game.lol.dto.rift.common.RiftPlayerDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.req.RiftPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.req.RiftPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.res.RiftPlayerHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.res.LolPlayerHistoryResponseSimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.res.RiftTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.rift.res.RiftPlayerResultHistoryResponseDetailDto;
import com.example.naejeonhajab.domain.game.lol.dto.common.res.LolPlayerResultHistoryResponseSimpleDto;
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

import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_TITLE_NOT_NULL;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game/lol/rift")
public class RiftController {

    private final RiftServiceImpl lolService;

    @PostMapping
    public ApiResponse<RiftTeamResponseDto> createTeam(@RequestBody @Valid RiftPlayerHistoryRequestDto dto) {
        RiftTeamResponseDto result = lolService.createTeam(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @PostMapping("/playerHistory")
    public ApiResponse<RiftTeamResponseDto> createPlayerHistoryAndTeam(@RequestBody @Valid RiftPlayerHistoryRequestDto dto, @AuthenticationPrincipal AuthUser authUser) {
        if (dto.getPlayerHistoryTitle() == null || dto.getPlayerHistoryTitle().isBlank()) {
            throw new LolException(LOL_TITLE_NOT_NULL);
        }
        for (RiftPlayerDto riftPlayerRequestDto : dto.getPlayerDtos()) {
            riftPlayerRequestDto.updateMmr(riftPlayerRequestDto.getTier().getScore());
        }
        RiftTeamResponseDto result = lolService.createPlayerHistoryAndTeam(dto, authUser);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @PostMapping("/playerResultHistory")
    public ApiResponse<Void> createResultTeam(@RequestBody @Valid RiftPlayerResultHistoryRequestDto dto, @AuthenticationPrincipal AuthUser authUser) {
        lolService.createResultTeam(dto,authUser);
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
    public ApiResponse<Page<LolPlayerHistoryResponseSimpleDto>> getSimpleTeam(@PathVariable int page, @AuthenticationPrincipal AuthUser authUser){
        Pageable pageable = PageRequest.of(page - 1, 3, Sort.Direction.DESC,"createdAt");
        Page<LolPlayerHistoryResponseSimpleDto> result = lolService.getSimpleTeam(authUser,pageable);
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
    public ApiResponse<Page<LolPlayerResultHistoryResponseSimpleDto>> getSimpleResultTeam(@PathVariable int page, @AuthenticationPrincipal AuthUser authUser){
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC,"createdAt");
        Page<LolPlayerResultHistoryResponseSimpleDto> result = lolService.getSimpleResultTeam(authUser,pageable);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
