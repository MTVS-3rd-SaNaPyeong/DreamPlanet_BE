package com.sanapyeong.mtvs_3rd_dreamplanet.inventory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlanetType {

    BLOCK("BLOCK_PLANET", "블록 행성"),
    MUSIC("MUSIC_PLANET", "음악 행성");

    private final String key;
    private final String title;
}
