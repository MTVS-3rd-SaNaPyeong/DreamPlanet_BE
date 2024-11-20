package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="TBL_TEMP_BLOCK_PLANET_STORAGE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TempBlockPlanetStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playedBlockPlanetId;

    private Long idx;

    private String colorDotImageUrl;

    private String blackAndWhiteImageUrl;

    public TempBlockPlanetStorage(
            Long playedBlockPlanetId,
            Long i,
            String colorDotImageURL,
            String blackAndWhiteDotImageURL
    ) {
        this.playedBlockPlanetId = playedBlockPlanetId;
        this.idx = i;
        this.colorDotImageUrl = colorDotImageURL;
        this.blackAndWhiteImageUrl = blackAndWhiteDotImageURL;
    }
}
