package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.enums.PlanetType;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SaveInventoryDTO {

    // 작품의 행성 종류
    private PlanetType planetType;

    // 작품이 만들어진 행성 번호
    private Long playedPlanetId;

    // 저장 여부
    private Boolean storedStatus;

}
