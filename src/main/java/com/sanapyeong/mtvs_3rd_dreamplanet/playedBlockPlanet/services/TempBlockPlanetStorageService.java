package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.TempBlockPlanetStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.repositories.TempBlockPlanetStorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TempBlockPlanetStorageService {

    private final TempBlockPlanetStorageRepository tbpStorageRepository;

    public TempBlockPlanetStorageService(TempBlockPlanetStorageRepository tbpStorageRepository) {
        this.tbpStorageRepository = tbpStorageRepository;
    }

    @Transactional
    public void saveDotImageURL(
            Long playedBlockPlanetId,
            long i,
            String colorDotImageURL,
            String blackAndWhiteDotImageURL
    ) {

        TempBlockPlanetStorage tbpStorage
                = new TempBlockPlanetStorage(
                playedBlockPlanetId,
                i,
                colorDotImageURL,
                blackAndWhiteDotImageURL);

        tbpStorageRepository.save(tbpStorage);
    }
}
