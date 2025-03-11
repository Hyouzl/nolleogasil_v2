/**
 * 맛집메이트의 채팅방 Entity 객체 필드 및 연관관계를 나타내고, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.entity.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Chatroom")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @Column(name = "chatroomId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomId;
    private String roomName; //채팅방 이름

    @ManyToOne(fetch = FetchType.LAZY) // 챗방 -- 유저(개설자) 다대일
    @JoinColumn(name = "masterId")
    private Users users;

    @Column(name = "maxNum")
    private int maxNum; //채팅방 최대 참여자수

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mateId", nullable = false, unique = true)
    private Mate mate;

    @OneToMany(mappedBy = "chatRoom" ,cascade = CascadeType.ALL, orphanRemoval = true) // 챗방 -- 메이트멤버 (일대다관계)
    private List<MateMember> mateMembers;


    public static ChatRoom creatChatroom(Mate savedMate,Users users) {
        return ChatRoom.builder()
                .roomName(savedMate.getTitle())
                .users(users)
                .maxNum(savedMate.getCount())
                .mate(savedMate)
                .mateMembers(new ArrayList<>())
                .build();
    }


}
