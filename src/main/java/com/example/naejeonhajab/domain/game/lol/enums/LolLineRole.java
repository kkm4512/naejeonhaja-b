package com.example.naejeonhajab.domain.game.lol.enums;

import lombok.Getter;

@Getter
public enum LolLineRole {
    MAINLINE,
    SUBLINE;

    LolLineRole() {
    }

    public boolean isMainRole(){
        return this == MAINLINE;
    }
}
