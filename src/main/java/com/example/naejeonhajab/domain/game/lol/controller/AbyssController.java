package com.example.naejeonhajab.domain.game.lol.controller;

import com.example.naejeonhajab.common.exception.LolException;
import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.common.response.enums.BaseApiResponse;
import com.example.naejeonhajab.domain.dto.common.Ids;
import com.example.naejeonhajab.domain.game.lol.dto.common.LolTeamResponseDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerHistoryUpdateRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerHistory.LolPlayerResultHistoryUpdateRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.req.playerResultHistory.LolPlayerResultHistoryRequestDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistoryDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerHistory.LolPlayerHistorySimpleDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistoryDto;
import com.example.naejeonhajab.domain.game.lol.dto.res.playerResultHistory.LolPlayerResultHistorySimpleDto;
import com.example.naejeonhajab.domain.game.lol.service.AbyssServiceImpl;
import com.example.naejeonhajab.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

import static com.example.naejeonhajab.common.response.enums.BaseApiResponse.SUCCESS;
import static com.example.naejeonhajab.common.response.enums.LolApiResponse.LOL_TITLE_NOT_NULL;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game/lol/abyss")
public class AbyssController {

    private final AbyssServiceImpl lolService;

    @Operation(
            summary = "리그오브레전드 칼바람 팀 생성",
            requestBody = @RequestBody(
                    required = true,
                    description = "팀 생성에 필요한 플레이어 정보",
                    content = @Content(schema = @Schema(implementation = LolPlayerHistoryRequestDto.class))
            )
    )
    @PostMapping
    public ApiResponse<LolTeamResponseDto> createTeam(@RequestBody @Valid LolPlayerHistoryRequestDto dto) {
        LolTeamResponseDto result = lolService.createTeam(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @Operation(
            summary = "리그오브레전드 칼바람 팀 생성 후 히스토리 저장",
            requestBody = @RequestBody(
                    required = true,
                    description = "팀 생성에 필요한 플레이어 정보",
                    content = @Content(schema = @Schema(implementation = LolPlayerHistoryRequestDto.class))
            )
    )
    @PostMapping("/playerHistory")
    public ApiResponse<LolTeamResponseDto> createTeamAndSavePlayerHistory(@RequestBody @Valid LolPlayerHistoryRequestDto dto, @AuthenticationPrincipal AuthUser authUser) {
        if (dto.getPlayerHistoryTitle() == null || dto.getPlayerHistoryTitle().isBlank()) {
            throw new LolException(LOL_TITLE_NOT_NULL);
        }
        LolTeamResponseDto result = lolService.createTeamAndSavePlayerHistory(dto, authUser);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @Operation(
            summary = "리그오브레전드 칼바람 팀 히스토리 수정",
            description = "플레이어 히스토리 ID를 기반으로 해당 팀 히스토리의 제목을 수정합니다.",
            parameters = {
                    @Parameter(
                            name = "playerHistoryId",
                            description = "수정할 히스토리의 ID",
                            required = true
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    description = "수정할 히스토리의 제목",
                    content = @Content(
                            schema = @Schema(implementation = LolPlayerHistoryUpdateRequestDto.class)
                    )
            )
    )
    @PutMapping("/playerHistory/{playerHistoryId}")
    public ApiResponse<Void> updatePlayerHistory(
            @PathVariable Long playerHistoryId,
            @RequestBody @Valid LolPlayerHistoryUpdateRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser) {

        lolService.updatePlayerHistory(playerHistoryId, dto, authUser);
        return ApiResponse.of(SUCCESS);
    }

    @Operation(
            summary = "리그오브레전드 칼바람 팀 히스토리 삭제",
            description = "플레이어 히스토리 ID를 기반으로 해당 팀 히스토리를 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "playerHistoryId",
                            description = "삭제할 히스토리의 ID",
                            required = true
                    )
            }
    )
    @DeleteMapping("/playerHistory/{playerHistoryId}")
    public ApiResponse<Void> deletePlayerHistory(
            @PathVariable Long playerHistoryId,
            @AuthenticationPrincipal AuthUser authUser) {
        lolService.deletePlayerHistory(playerHistoryId, authUser);
        return ApiResponse.of(SUCCESS);
    }

    @Operation(
            summary = "리그오브레전드 칼바람 팀 히스토리 삭제 (여러 개)",
            description = "여러 개의 플레이어 히스토리 ID를 전달받아 해당 히스토리들을 삭제합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "삭제할 히스토리 ID 리스트 (예: { \"ids\": [1, 2, 3] })",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Ids.class)
                    )
            )
    )

    @PostMapping("/playerHistory1")
    public ApiResponse<Void> deletePlayerHistoryAll(@RequestBody @Valid Ids ids, @AuthenticationPrincipal AuthUser authUser) {
        // lolService.deletePlayerHistoryAll(ids, authUser);
        return ApiResponse.of(SUCCESS);
    }


    @PostMapping("/playerResultHistory")
    public ApiResponse<Void> saveResultHistory(@RequestBody @Valid LolPlayerResultHistoryRequestDto dto, @AuthenticationPrincipal AuthUser authUser) {
        lolService.saveResultHistory(dto,authUser);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }

    @PutMapping("/playerResultHistory/{playerResultHistoryId}")
    public ApiResponse<Void> updatePlayerResultHistory(@PathVariable Long playerResultHistoryId, @RequestBody @Valid LolPlayerResultHistoryUpdateRequestDto dto, @AuthenticationPrincipal AuthUser authUser) {
        lolService.updatePlayerResultHistory(playerResultHistoryId,dto,authUser);
        return ApiResponse.of(SUCCESS);
    }

    @DeleteMapping("/playerResultHistory/{playerResultHistoryId}")
    public ApiResponse<Void> deletePlayerResultHistory(@PathVariable Long playerResultHistoryId, @AuthenticationPrincipal AuthUser authUser) {
        lolService.deletePlayerResultHistory(playerResultHistoryId,authUser);
        return ApiResponse.of(SUCCESS);
    }

    @DeleteMapping("/playerResultHistory")
    public ApiResponse<Void> deleteAllPlayerResultHistory(@RequestBody @Valid List<LolPlayerResultHistorySimpleDto> dtos, @AuthenticationPrincipal AuthUser authUser) {
        lolService.deleteAllPlayerResultHistory(dtos,authUser);
        return ApiResponse.of(SUCCESS);
    }

    // title, 10명의 유저 정보
    @GetMapping("/playerHistory/detail/{playerHistoryId}")
    public ApiResponse<LolPlayerHistoryDto> getPlayerHistoryDetailTeam(@PathVariable Long playerHistoryId){
        LolPlayerHistoryDto result = lolService.getPlayerHistoryDetailTeam(playerHistoryId);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }


    // id, title만
    @GetMapping("/playerHistory/simple/{page}")
    public ApiResponse<Page<LolPlayerHistorySimpleDto>> getPlayerHistorySimpleTeam(@PathVariable int page, @AuthenticationPrincipal AuthUser authUser){
        Pageable pageable = PageRequest.of(page - 1, 3, Sort.Direction.DESC,"createdAt");
        Page<LolPlayerHistorySimpleDto> result = lolService.getPlayerHistorySimpleTeam(authUser,pageable);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    // title, 10명의 유저 정보
    @GetMapping("/playerResultHistory/detail/{playerResultHistoryId}")
    public ApiResponse<LolPlayerResultHistoryDto> getResultHistoryDetailTeam(@PathVariable Long playerResultHistoryId){
        LolPlayerResultHistoryDto result = lolService.getResultHistoryDetailTeam(playerResultHistoryId);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    // id, title만
    @GetMapping("/playerResultHistory/simple/{page}")
    public ApiResponse<Page<LolPlayerResultHistorySimpleDto>> getResultHistorySimpleTeam(@PathVariable int page, @AuthenticationPrincipal AuthUser authUser){
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC,"createdAt");
        Page<LolPlayerResultHistorySimpleDto> result = lolService.getResultHistorySimpleTeam(authUser,pageable);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    //클라이언트로부터 요청받은 제목과 유사한것들을 반환시키기 (10개씩 반환시키면 적당할듯)
    @GetMapping("/simpleSearch")
    public ApiResponse<List<LolPlayerHistorySimpleDto>> playerHistorySearch(
            @RequestParam(required = false) String playerHistoryTitle,
            @RequestParam(required = false) int page,
            @AuthenticationPrincipal AuthUser authUser
    ){
        Pageable pageable = PageRequest.of(page - 1,3, Sort.Direction.DESC,"createdAt");
        List<LolPlayerHistorySimpleDto> result = lolService.playerHistorySearch(playerHistoryTitle,authUser,pageable);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }

    @GetMapping("/detailSearch")
    public ApiResponse<Page<LolPlayerResultHistorySimpleDto>> playerResultHistorySearch(
            @RequestParam(required = false) String playerResultHistoryTitle,
            @RequestParam(required = false) int page,
            @AuthenticationPrincipal AuthUser authUser
    ){
        Pageable pageable = PageRequest.of(page - 1,10, Sort.Direction.DESC,"createdAt");
        Page<LolPlayerResultHistorySimpleDto> result = lolService.playerResultHistorySearch(playerResultHistoryTitle,authUser,pageable);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
