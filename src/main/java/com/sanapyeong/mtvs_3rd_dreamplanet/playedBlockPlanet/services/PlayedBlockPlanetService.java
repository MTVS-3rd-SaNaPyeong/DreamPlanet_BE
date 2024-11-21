package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto.PlayedBlockPlanetCreateRequestDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.repositories.PlayedBlockPlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional
    public Long createPlayedBlockPlanet(PlayedBlockPlanet playedBlockPlanet) {
        return playedBlockPlanetRepository.save(playedBlockPlanet).getId();
    }

    @Transactional
    public void saveCompletedWork(Long playedBlockPlanetId, String storedURL) {

        Optional<PlayedBlockPlanet> optionalPlayedBlockPlanet = playedBlockPlanetRepository.findById(playedBlockPlanetId);

        // 엔티티가 존재하는지 확인
        if (optionalPlayedBlockPlanet.isPresent()) {
            PlayedBlockPlanet playedBlockPlanet = optionalPlayedBlockPlanet.get();

            // completed work 정보 업데이트
            if (storedURL != null) {
                playedBlockPlanet.setCompletedWork(storedURL);
            }

            // 자동으로 변경된 내용이 영속성 컨텍스트에 반영됨 (Transactional 덕분)
        } else {
            throw new IllegalArgumentException("ID에 해당하는 블록 행성이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void saveIdx(Long playedBlockPlanetId, Long idx) {
        Optional<PlayedBlockPlanet> optionalPlayedBlockPlanet = playedBlockPlanetRepository.findById(playedBlockPlanetId);

        // 엔티티가 존재하는지 확인
        if (optionalPlayedBlockPlanet.isPresent()) {
            PlayedBlockPlanet playedBlockPlanet = optionalPlayedBlockPlanet.get();

            // completed work 정보 업데이트
            if (idx != null) {
                playedBlockPlanet.setIdx(idx);
            }

            // 자동으로 변경된 내용이 영속성 컨텍스트에 반영됨 (Transactional 덕분)
        } else {
            throw new IllegalArgumentException("ID에 해당하는 블록 행성이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void saveColorDotImage(Long playedBlockPlanetId, String storedURL){
        Optional<PlayedBlockPlanet> optionalPlayedBlockPlanet = playedBlockPlanetRepository.findById(playedBlockPlanetId);

        // 엔티티가 존재하는지 확인
        if (optionalPlayedBlockPlanet.isPresent()) {
            PlayedBlockPlanet playedBlockPlanet = optionalPlayedBlockPlanet.get();

            // completed work 정보 업데이트
            if (storedURL != null) {
                playedBlockPlanet.setColorDotImage(storedURL);
            }

            // 자동으로 변경된 내용이 영속성 컨텍스트에 반영됨 (Transactional 덕분)
        } else {
            throw new IllegalArgumentException("ID에 해당하는 블록 행성이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void saveBlackAndWhiteDotImage(Long playedBlockPlanetId, String storedURL){
        Optional<PlayedBlockPlanet> optionalPlayedBlockPlanet = playedBlockPlanetRepository.findById(playedBlockPlanetId);

        // 엔티티가 존재하는지 확인
        if (optionalPlayedBlockPlanet.isPresent()) {
            PlayedBlockPlanet playedBlockPlanet = optionalPlayedBlockPlanet.get();

            // completed work 정보 업데이트
            if (storedURL != null) {
                playedBlockPlanet.setBlackAndWhiteDotImage(storedURL);
            }

        } else {
            throw new IllegalArgumentException("ID에 해당하는 블록 행성이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void savePrompt(Long playedBlockPlanetId, String prompt) {

        Optional<PlayedBlockPlanet> optionalPlayedBlockPlanet = playedBlockPlanetRepository.findById(playedBlockPlanetId);

        // 엔티티가 존재하는지 확인
        if (optionalPlayedBlockPlanet.isPresent()) {
            PlayedBlockPlanet playedBlockPlanet = optionalPlayedBlockPlanet.get();

            // completed work 정보 업데이트
            if (prompt != null) {
                playedBlockPlanet.setPrompt(prompt);
            }

        } else {
            throw new IllegalArgumentException("ID에 해당하는 블록 행성이 존재하지 않습니다.");
        }
    }
}
