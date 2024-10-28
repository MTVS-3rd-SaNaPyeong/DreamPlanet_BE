package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities;

import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="TBL_PARTICIPANT")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "played_block_planet_id")
    private PlayedBlockPlanet playedBlockPlanet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Participant(PlayedBlockPlanet playedBlockPlanet, User user) {
        this.playedBlockPlanet = playedBlockPlanet;
        this.user = user;
    }
}
