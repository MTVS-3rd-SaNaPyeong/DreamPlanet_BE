package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities;

import com.sanapyeong.global.database.utils.EntityTimestamp;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@Table(name="TBL_PLAYED_BLOCK_PLANET")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PlayedBlockPlanet extends EntityTimestamp {

    // 최초 행성 플레이 시작 시에는 id, photon_planet_id, participants 3가지 항목만 입력
    // 행성 시작 후, prompt 전달 받음.
    // 이미지 생성 요청을 통해 colorDotImage, blackAndWhiteDotImage 이미지 받아옴.
    // 행성 플레이 종료 후, 완성 이미지 받아옴.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 포톤 서버 자체 id
    private String photonPlanetId;

    // Participant 엔티티에서 playedblockplanet이란 필드가 이 관계를 소유 중
    // 즉, Participant 클래스에 해당 필드가 있어야 하고, 그것이 ManyToOne과 같은 어노테이션을 통해 매핑되어야 함.
    // cascade를 통해서 부모 엔티티 삭제 시, 자식 엔티티도 모두 삭제
    @OneToMany(mappedBy = "playedBlockPlanet", cascade = CascadeType.REMOVE)
    private Set<Participant> participants;

    // 이미지 생성을 위해 입력된 텍스트
    private String prompt;

    // 이미지는 AWS S3 서버에 저장한 후, 반환받은 URL 주소를 저장
    // 컬러 도트 이미지
    private String colorDotImage;
    // 흑백 도트 이미지
    private String blackAndWhiteDotImage;
    // 완성 작품 이미지
    private String completedWork;

    public PlayedBlockPlanet(String photonPlanetId, Set<Participant> participants) {
        this.photonPlanetId = photonPlanetId;
        this.participants = participants;
    }

    public PlayedBlockPlanet(PlayedBlockPlanet playedBlockPlanet) {
        this.id = playedBlockPlanet.getId();
        this.photonPlanetId = playedBlockPlanet.getPhotonPlanetId();
        this.participants = playedBlockPlanet.getParticipants();
        this.prompt = playedBlockPlanet.getPrompt();
        this.colorDotImage = playedBlockPlanet.getColorDotImage();
        this.blackAndWhiteDotImage = playedBlockPlanet.getBlackAndWhiteDotImage();
        this.completedWork = playedBlockPlanet.getCompletedWork();
    }

    public PlayedBlockPlanet(String photonPlanetId) {
        this.photonPlanetId = photonPlanetId;
    }
}
