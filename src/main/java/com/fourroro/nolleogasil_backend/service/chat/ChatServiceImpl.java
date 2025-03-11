/**
 * 맛집메이트의 채팅방 관리를 위한 Service 입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.service.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatDto;
import com.fourroro.nolleogasil_backend.entity.chat.Chat;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.repository.chat.ChatRepository;
import com.fourroro.nolleogasil_backend.service.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;
    private final UsersService usersService;


    // 메이트 신청 후 수락한 뒤
    // 첫 채팅방 구독 시
    @Override
    public void enterChatRoom(Long chatroomId, Long usersId) {

        Users users = usersService.findByUsersId(usersId);
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatroomId);

        String enterMessage = users.getNickname() + "님이 입장하셨습니다.";
        Chat chat = Chat.createChat(chatRoom,users,enterMessage,LocalDateTime.now(),"enter");
        chatRepository.save(chat);

    }

    @Override
    public void sendMessage(Long chatroomId, Long usersId, String sendMessage) {

        Users users = usersService.findByUsersId(usersId);
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatroomId);

        Chat chat = Chat.createChat(chatRoom,users,sendMessage,LocalDateTime.now(),"talk");
        System.out.println(chat);
        chatRepository.save(chat);
    }



    @Override
    public List<ChatDto.ResponseChatDTO> getChatList(Long chatroomId) {

        List<Chat> chatList = new ArrayList<>();

        try {

            chatList = chatRepository.findChatsByChatRoomChatroomId(chatroomId);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<ChatDto.ResponseChatDTO> chatDtos = chatList.stream()
                                .map(ChatDto.ResponseChatDTO::toChatDto)
                                .collect(Collectors.toList());

        return chatDtos;

    }



}
