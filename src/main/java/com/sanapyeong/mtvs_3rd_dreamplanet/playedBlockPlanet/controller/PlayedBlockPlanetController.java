package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.controller;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto.PlayedBlockPlanetCreateRequestDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.PlayedBlockPlanetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="", description = "a")
@RestController
@RequestMapping("/v1/dev")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class PlayedBlockPlanetController {

    private final PlayedBlockPlanetService playedBlockPlanetService;

    @Autowired
    public PlayedBlockPlanetController(
            PlayedBlockPlanetService playedBlockPlanetService
    ) {
        this.playedBlockPlanetService = playedBlockPlanetService;
    }

    @PostMapping("/played-block-planets")
    public ResponseEntity<?> createPlayedBlockPlanet(@RequestParam String photonPlanetId) {

        PlayedBlockPlanet playedBlockPlanet = new PlayedBlockPlanet(photonPlanetId);

        Long id = playedBlockPlanetService.createPlayedBlockPlanet(playedBlockPlanet);

        return ResponseEntity.ok().body(id);
    }
}
