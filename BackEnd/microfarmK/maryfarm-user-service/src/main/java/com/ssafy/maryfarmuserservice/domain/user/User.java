package com.ssafy.maryfarmuserservice.domain.user;

import com.ssafy.maryfarmuserservice.domain.Land;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
// 프록시 객체 생성만을 위한 생성자라 Protected로 사용제한
// 정적 팩토리 메서드에 필요한 생성자는 롬복 사용하지 않고 직접 구현
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {
    @Id
    @Column(name = "user_id")
    private String id;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Tier tier;
    @Embedded
    private Land land;
    private String profilePath;
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setLand(Land land) {
        this.land = land;
    }
    public void setProfilePath(String path) {
        this.profilePath = path;
    }
    // 정적 팩토리 메서드
    public static User of(String id, String nickname, Tier tier) {
        User user = new User();
        user.id = id;
        user.nickname = nickname;
        user.tier = tier;
        return user;
    }
}
