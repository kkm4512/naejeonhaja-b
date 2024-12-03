package com.example.naejeonhajab.domain.game.lol.enums;

import lombok.Getter;

@Getter
public enum LineRole {
    MAINLINE,
    SUBLINE;

    LineRole() {
    }

    public boolean isMainRole(){
        return this == MAINLINE;
    }
}
