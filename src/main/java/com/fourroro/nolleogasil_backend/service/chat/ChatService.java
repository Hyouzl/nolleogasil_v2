package com.fourroro.nolleogasil_backend.service.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatDto;

import java.util.List;

public interface ChatService {

    void enterChatRoom(Long chatroomId, Long usersId);
    void sendMessage(Long chatroomId, Long usersId, String sendMessage);
    List<ChatDto.ResponseChatDTO> getChatList(Long chatRoomId);

}
