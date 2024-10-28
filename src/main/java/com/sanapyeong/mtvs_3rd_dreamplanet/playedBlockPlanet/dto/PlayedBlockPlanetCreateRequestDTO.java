package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.Participant;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PlayedBlockPlanetCreateRequestDTO {

    // 포톤 서버 자체 id
    private String photonPlanetId;

    private Set<Participant> participants;

}
