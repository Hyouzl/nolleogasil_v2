package com.fourroro.nolleogasil_backend.dto.chatgpt;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/**
 * ChatGpt API에 보낼 메세지들을 담은 requestDTO 클래스 입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Data
public class ChatGptRequestDto {
    private String model;
    private List<ChatGptMessage> messages;

    public ChatGptRequestDto(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new ChatGptMessage("user", prompt));
    }
}
