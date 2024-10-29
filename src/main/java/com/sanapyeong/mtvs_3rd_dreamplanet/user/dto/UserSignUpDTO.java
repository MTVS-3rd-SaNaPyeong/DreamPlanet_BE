package com.sanapyeong.mtvs_3rd_dreamplanet.user.dto;

import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserSignUpDTO {

    private String nickname;

    private String loginId;

    private String loginPw;

}
