package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrainColor {

    BASIC("BASIC", "베이직 배경"),
    RWG("RWG", "빨간색, 흰색, 초록색"),
    OYW("OYW", "오렌지색, 노란색, 흰색");

    private final String key;
    private final String title;

}
