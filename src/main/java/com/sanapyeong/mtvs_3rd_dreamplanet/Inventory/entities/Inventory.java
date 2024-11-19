package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities;

import com.sanapyeong.global.database.utils.EntityTimestamp;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.enums.PlanetType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="TBL_INVENTORY")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Inventory extends EntityTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 인벤토리 소유자
    private Long userId;

    // 작품의 행성 종류
    private PlanetType planetType;

    // 작품이 만들어진 행성 번호
    private Long playedPlanetId;

    // 저장 여부
    private Boolean storedStatus;

//    // 게시판에서 게시된 위치 (좌상단부터 1 ~ 우하단 n)
//    // 미게시된 작품의 번호는 0으로 설정
//    private Long postedLocation;

//    @ElementCollection
//    private List<Long> myUniverseTrainList;

    public Inventory(Long userId, PlanetType planetType, Long playedPlanetId, Boolean storedStatus) {
        this.userId = userId;
        this.planetType = planetType;
        this.playedPlanetId = playedPlanetId;
        this.storedStatus = storedStatus;
//        this.postedLocation = 0L;
    }
}
