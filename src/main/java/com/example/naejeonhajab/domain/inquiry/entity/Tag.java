package com.example.naejeonhajab.domain.inquiry.entity;

import com.example.naejeonhajab.domain.inquiry.dto.InquiryRequestDto;
import com.example.naejeonhajab.domain.inquiry.enums.TagType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 문의 ID (PK)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;


    public static List<Tag> of(InquiryRequestDto dto, Inquiry inquiry) {
        return dto.getTags()
                .stream()
                .map(tagType -> Tag.of(tagType, inquiry)) // TagType을 넘겨서 Tag 생성
                .toList();
    }

    public static Tag of(TagType tag, Inquiry inquiry) {
        return Tag.builder()
                .tag(tag)
                .inquiry(inquiry)
                .build();
    }



}
