package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto.PlayedBlockPlanetCreateRequestDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.repositories.PlayedBlockPlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayedBlockPlanetService {

    private final PlayedBlockPlanetRepository playedBlockPlanetRepository;

    @Autowired
    public PlayedBlockPlanetService(
            PlayedBlockPlanetRepository playedBlockPlanetRepository
    ) {
        this.playedBlockPlanetRepository = playedBlockPlanetRepository;
    }

    public PlayedBlockPlanet findPlayedBlockPlanetById(Long id) {
        return new PlayedBlockPlanet(playedBlockPlanetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("플레이 중인 블록행성을 찾을 수 없습니다.")));
    }

    public Long createPlayedBlockPlanet(PlayedBlockPlanet playedBlockPlanet) {
        return playedBlockPlanetRepository.save(playedBlockPlanet).getId();
    }
}
