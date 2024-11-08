package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto;

import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CompletedWorkSaveDTO {

    private Long playedBlockPlanetId;
    
    private MultipartFile completedWork;
}
