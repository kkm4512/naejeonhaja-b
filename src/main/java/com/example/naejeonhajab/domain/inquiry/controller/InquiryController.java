package com.example.naejeonhajab.domain.inquiry.controller;

import com.example.naejeonhajab.common.response.ApiResponse;
import com.example.naejeonhajab.domain.inquiry.dto.InquiryRequestDto;
import com.example.naejeonhajab.domain.inquiry.dto.InquiryResponseDto;
import com.example.naejeonhajab.domain.inquiry.service.InquiryService;
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

import static com.example.naejeonhajab.common.response.enums.BaseApiResponse.SUCCESS;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    // 문의글 등록
    @PostMapping
    public ApiResponse<Void> createInquiry(
            @Valid @RequestBody InquiryRequestDto dto,
            @AuthenticationPrincipal AuthUser authuser
    ) {
        inquiryService.createInquiry(dto,authuser);
        return ApiResponse.of(SUCCESS);
    }

    // 전체 문의글 조회
    @GetMapping
    public ApiResponse<Page<InquiryResponseDto>> getAllInquiries(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC,"createdAt");
        Page<InquiryResponseDto> result = inquiryService.getAllInquiries(pageable);
        return ApiResponse.of(SUCCESS,result);
    }

    // 상세 문의글 조회
    @GetMapping("/{id}")
    public ApiResponse<InquiryResponseDto> getInquiryById(@PathVariable Long id) {
        InquiryResponseDto result = inquiryService.getInquiryById(id);
        return ApiResponse.of(SUCCESS,result);
    }

    // 문의글 수정
    @PutMapping("/{id}")
    public ApiResponse<Void> updateInquiry(
            @PathVariable Long id,
            @Valid @RequestBody InquiryRequestDto dto,
            @AuthenticationPrincipal AuthUser authuser
    ) {
        inquiryService.updateInquiry(id, dto,authuser);
        return ApiResponse.of(SUCCESS);
    }

    // 문의글 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteInquiry(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authuser
    ) {
        inquiryService.deleteInquiry(id,authuser);
        return ApiResponse.of(SUCCESS);
    }
}
