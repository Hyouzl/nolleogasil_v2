/**
 * 맛집메이트의 채팅 메세지를 위한 Dto 객체를 구성, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fourroro.nolleogasil_backend.entity.chat.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


public class ChatDto {

    @Builder
    @Getter
    public static class RequestChatDTO {
        private Long chatroomId;
        private Long userId;
        private String nickname;
        private String message;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime sendDate;
        private String messageType;
    }

    @Builder
    @Getter
    public static class ResponseChatDTO {
        private Long chatId;
        private Long chatroomId;
        private Long usersId;
        private String nickname;
        private String message;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime sendDate;
        private String messageType;
        public static ResponseChatDTO toChatDto(Chat chat) {
            return ResponseChatDTO.builder()
                    .chatId(chat.getChatId())
                    .usersId(chat.getUsers().getUsersId())
                    .chatroomId(chat.getChatRoom().getChatroomId())
                    .message(chat.getMessage())
                    .sendDate(chat.getSendDate())
                    .nickname(chat.getUsers().getNickname())
                    .messageType(chat.getMessageType())
                    .build();
        }
    }


}
