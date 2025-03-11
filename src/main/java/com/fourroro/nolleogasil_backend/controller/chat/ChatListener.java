/**
 * 이 클래스는 맛집메이트 채팅관리를 위한 Controller입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.controller.chat;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourroro.nolleogasil_backend.dto.chat.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "chat.queue")
    public void receiveMessage(ChatDto.ResponseChatDTO message) {
        try {
            Long chatroomId = message.getChatroomId();
            // WebSocket을 통 클라이언트로 브로드캐스트
            messagingTemplate.convertAndSend("/chat.exchange/room." + chatroomId, message);
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }
    }

}

