package com.sanapyeong.mtvs_3rd_dreamplanet.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserColor {


    ORANGE("COLOR_ORANGE", "오렌지색"),
    BLUE("COLOR_BLUE", "파란색"),
    GREEN("COLOR_GREEN", "초록색"),
    WHITE("COLOR_WHITE", "흰색");

    private final String key;
    private final String title;
}
