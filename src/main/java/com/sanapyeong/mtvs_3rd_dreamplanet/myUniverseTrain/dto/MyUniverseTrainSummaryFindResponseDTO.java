package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto;

import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MyUniverseTrainSummaryFindResponseDTO {

    // my universe train ID
    private Long id;

    private Long userId;

    private String trainName;

    private String uniqueCode;

    public MyUniverseTrainSummaryFindResponseDTO(Object[] result) {

        // my universe train id
        this.id = (Long) result[0];

        // userId
        this.userId = (Long) result[1];

        // trainName
        this.trainName = (String) result[2];

        // uniqueCode
        this.uniqueCode = (String) result[3];
    }
}
