/**
 * 이 클래스는 맛집메이트 채팅관리를 위한 Controller입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.controller.chat;


import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.chat.ChatDto;
import com.fourroro.nolleogasil_backend.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StompRabbitController {

    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;
    private final TokenProvider tokenProvider;

    @MessageMapping("chat.enter")
    public void enterTest(@Payload Map<String, Object> message) {
        Long userId = Long.valueOf(message.get("userId").toString());

        Long chatroomId = Long.parseLong(message.get("chatroomId").toString());

        chatService.enterChatRoom(chatroomId,userId);

        rabbitTemplate.convertAndSend("amq.topic","room." + chatroomId, message);
    }

    @MessageMapping("chat.send")
    public void sendMessage(@Payload ChatDto.RequestChatDTO message) {

        if (message.getChatroomId() == null || message.getUserId() == null) {
            System.err.println("chatroomId 또는 userId가 없습니다!");
            return;
        }

        chatService.sendMessage(message.getChatroomId(), message.getUserId(), message.getMessage());

        try {
            rabbitTemplate.convertAndSend("amq.topic","room." + message.getChatroomId(), message);
        } catch  (Exception e) {
            System.err.println("Failed to send message to RabbitMQ: " + e.getMessage());
        }
    }
}
