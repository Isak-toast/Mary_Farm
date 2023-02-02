package com.ssafy.myfarm.domain.chat;

import com.ssafy.myfarm.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
// 프록시 객체 생성만을 위한 생성자라 Protected로 사용제한
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "room_id")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user1_id")
    private User user1;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user2_id")
    private User user2;

    public static Room of(User user1, User user2) {
        Room room = new Room();
        room.user1 = user1;
        room.user2 = user2;
        return room;
    }
}
