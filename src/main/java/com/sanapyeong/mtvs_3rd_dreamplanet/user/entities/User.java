package com.sanapyeong.mtvs_3rd_dreamplanet.user.entities;

import com.sanapyeong.global.database.utils.EntityTimestamp;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
        name="TBL_USER",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "nickname"),
                @UniqueConstraint(columnNames = "loginId")
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends EntityTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 닉네임
    //@Column(unique=true)
    private String nickname;

    // 유저 아이디
    //@Column(unique=true)
    private String loginId;

    // 유저 비밀번호 -> 해시함수 적용 필요
    private String loginPw;

    public User(String nickname, String loginId, String loginPw) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.loginPw = loginPw;
    }
}
