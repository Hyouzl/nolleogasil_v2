package com.fourroro.nolleogasil_backend.entity.chat;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Entity
@Table(name="Chat")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat {

    @Id
    @Column(name="chatId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY) //유저 -- 챗 (일대다관계)
    @JsonIgnore
    @JoinColumn(name="sender")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY) // 챗방 -- 챗 (일대다관계)
    @JsonIgnore
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDate;

    private String messageType;

    //구독한 챗방.보내는 유저.메세지 내용 을 받아 챗 객체 생성.
    public static Chat createChat(ChatRoom chatRoom, Users users, String message,LocalDateTime sendDate,String messageType) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .users(users)
                .message(message)
                .sendDate(sendDate)
                .messageType(messageType)
                .build();
    }


}
