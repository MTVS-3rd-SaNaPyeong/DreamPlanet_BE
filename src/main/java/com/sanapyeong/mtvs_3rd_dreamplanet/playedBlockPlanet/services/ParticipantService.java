package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.Participant;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(
            ParticipantRepository participantRepository
    ) {
        this.participantRepository = participantRepository;
    }

    public void registerParticipant(Participant participant) {
        participantRepository.save(participant);
    }
}
