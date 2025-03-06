package com.example.naejeonhajab.domain.inquiry.dto;

import com.example.naejeonhajab.domain.inquiry.enums.TagType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryRequestDto {
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(min = 1, max = 50, message = "제목은 최소 1글자 이상, 최대 50글자까지 가능합니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    @Size(min = 1, max = 1000, message = "내용은 최소 1글자 이상, 최대 1000글자까지 가능합니다.")
    private String content;
    @Valid
    @Size(max = 5, message = "최대 5개의 태그만 입력 가능합니다.")
    List<TagType> tags;
}
