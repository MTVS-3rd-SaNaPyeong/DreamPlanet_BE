package com.sanapyeong.mtvs_3rd_dreamplanet.inventory.entities;

import com.sanapyeong.global.database.utils.EntityTimestamp;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="TBL_POSTING_INFO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostingInfo extends EntityTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long inventoryId;

    private Long myUniverseTrainId;

    private Long postedLocation;

    public PostingInfo(Long inventoryId, Long myUniverseTrainId) {
        this.inventoryId = inventoryId;
        this.myUniverseTrainId = myUniverseTrainId;
        this.postedLocation = 0L;
    }
}
