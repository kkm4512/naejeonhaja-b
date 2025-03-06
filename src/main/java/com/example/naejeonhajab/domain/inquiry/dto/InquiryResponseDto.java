package com.example.naejeonhajab.domain.inquiry.dto;

import com.example.naejeonhajab.domain.inquiry.entity.Inquiry;
import com.example.naejeonhajab.domain.inquiry.enums.TagType;
import com.example.naejeonhajab.domain.user.dto.common.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponseDto {
    private Long id;
    private String title;
    private String content;
    List<TagDto> tags;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserDto userDto;

    public static List<InquiryResponseDto> of (List<Inquiry> inquiries) {
        return inquiries.stream().map(InquiryResponseDto::of).toList();
    }

    public static InquiryResponseDto of(Inquiry inquiry) {
        return new InquiryResponseDto(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                TagDto.of(inquiry.getTags()),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt(),
                UserDto.of(inquiry.getUser())
        );
    }
}
