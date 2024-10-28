package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
