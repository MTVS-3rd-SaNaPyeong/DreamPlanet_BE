package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
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

    private String trainName;

    private String uniqueCode;

    @Column(columnDefinition = "json")
    private String planetStatus;

    @Column(columnDefinition = "json")
    private String planetOrder;

    private List<BlockInventoryFindResponseDTO> blockTrainInfo;
}
