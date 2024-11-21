package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto;

import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DotImageUrlFindResponseDTO {

    private Long idx;

    private String colorDotImageUrl;

    private String blackAndWhiteDotImageUrl;

}
