package com.sanapyeong.mtvs_3rd_dreamplanet.inventory.dto;

import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BlockInventoryFindResponseDTO {

    // inventory id
    private Long id;

    // 작품이 만들어진 행성 번호
    private Long playedPlanetId;

    // 게시판에서 게시된 위치 (좌상단부터 1 ~ 우하단 n)
    // 미게시된 작품의 번호는 0으로 설정
    private Long postedLocation;

    // 완성 작품 URL
    private String completedWork;

    public BlockInventoryFindResponseDTO(Object[] result) {

        //inventoryId
        this.id = (Long) result[0];

        // playedPlanetId
        this.playedPlanetId = (Long) result[1];

        // postedLocation
        this.postedLocation = (Long) result[2];

        // completedWork
        this.completedWork = (String) result[3];

    }

}
