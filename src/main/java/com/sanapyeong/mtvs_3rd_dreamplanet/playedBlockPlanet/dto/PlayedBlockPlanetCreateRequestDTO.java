package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PlayedBlockPlanetCreateRequestDTO {

    // 포톤 서버 자체 id
    private String photonPlanetId;

    // private Set<User> participants;

    // 이미지 생성을 위해 입력된 텍스트
    private String prompt;

    // 이미지는 AWS S3 서버에 저장한 후, 반환받은 URL 주소를 저장
    // 컬러 도트 이미지
    private String colorDotImage;
    // 흑백 도트 이미지
    private String blackAndWhiteDotImage;
    // 완성 작품 이미지
    private String completedWork;
}
