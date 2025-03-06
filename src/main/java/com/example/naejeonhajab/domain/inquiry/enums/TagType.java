package com.example.naejeonhajab.domain.inquiry.enums;

import lombok.Getter;

@Getter
public enum TagType {
    MATCHING_ISSUE("매칭 문제"),
    TEAM_COMPOSITION("팀 구성"),
    ETC("기타"),
    BUG("버그"),
    FEATURE_REQUEST("기능 요청");

    private final String description;

    TagType(String description) {
        this.description = description;
    }
}

