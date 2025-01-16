package com.example.naejeonhajab.domain.game.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 여기가 문제인가
public class RiotImageDto {
    private String full;
    private String sprite;
    private String group;
    private int x;
    private int y;
    private int w;
    private int h;
}