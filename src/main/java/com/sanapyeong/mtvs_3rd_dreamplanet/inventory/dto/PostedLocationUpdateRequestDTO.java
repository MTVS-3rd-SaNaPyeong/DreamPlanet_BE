package com.sanapyeong.mtvs_3rd_dreamplanet.inventory.dto;

import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.enums.PlanetType;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostedLocationUpdateRequestDTO {

    // 변경될 작품의 inventory id
    // -> postingInfo id
    // 만약 빈 공간이었다면, 0으로 반환
    // private Long prevId;
    private Long prevPostingInfoId;

    // 변경할 작품의 inventory id
    // -> postingInfo id
    // private Long nextId;
    private Long nextPostingInfoId;

    // 우주열차 id
    private Long myUniverseTrainId;

    // 변경할 위치
    private Long postedLocation;

    // 행성 타입
    private PlanetType planetType;

}
