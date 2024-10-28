package com.sanapyeong.mtvs_3rd_dreamplanet.user.entities;

import com.sanapyeong.global.database.utils.EntityTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="TBL_USER")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends EntityTimestamp {

    @Id
    private Long id;

    // 유저 닉네임
    private String nickname;

    // 유저 아이디
    private String loginId;

    // 유저 비밀번호 -> 해시함수 적용 필요
    private String loginPw;

    public User(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.loginId = user.getLoginId();
        this.loginPw = user.getLoginPw();
    }
}
