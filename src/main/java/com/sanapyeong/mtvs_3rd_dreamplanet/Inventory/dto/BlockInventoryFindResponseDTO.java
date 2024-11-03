package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.enums.PlanetType;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BlockInventoryFindResponseDTO {

    // 작품이 만들어진 행성 번호
    private Long playedPlanetId;

    // 게시판에서 게시된 위치 (좌상단부터 1 ~ 우하단 n)
    // 미게시된 작품의 번호는 0으로 설정
    private Long postedLocation;

    // 완성 작품 URL
    private String completedWork;

    public BlockInventoryFindResponseDTO(Object[] result) {

        // playedPlanetId
        this.playedPlanetId = (Long) result[0];

        // postedLocation
        this.postedLocation = (Long) result[1];

        // completedWork
        this.completedWork = (String) result[2];

    }

}
