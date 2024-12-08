package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto;

import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.enums.TrainColor;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MyUniverseTrainFindResponseDTO {

    private Long id;

    private TrainColor trainColor;

    private String trainName;

    private String uniqueCode;

    @Column(columnDefinition = "json")
    private String planetStatus;

    @Column(columnDefinition = "json")
    private String planetOrder;

    private List<BlockInventoryFindResponseDTO> blockTrainInfo;
}
