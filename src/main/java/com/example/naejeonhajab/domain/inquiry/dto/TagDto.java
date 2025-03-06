package com.example.naejeonhajab.domain.inquiry.dto;

import com.example.naejeonhajab.domain.inquiry.entity.Tag;
import com.example.naejeonhajab.domain.inquiry.enums.TagType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private Long id;
    private TagType tag;

    public static List<TagDto> of(List<Tag> tags) {
        return tags
                .stream()
                .map(TagDto::of)
                .toList();
    }

    public static TagDto of(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getTag()
        );
    }
}
