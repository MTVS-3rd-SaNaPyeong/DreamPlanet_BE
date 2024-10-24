package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dev")
public class PlayedBlockPlanetController {

    @GetMapping("/played-block-planets")
    public String findAllPlayedBlockPlanets() {

        return "Played Block Planets";
    }
}
