package com.fourroro.nolleogasil_backend.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * ChatGpt API로 부터 받은 응답을 담은 responseDTO 클래스 입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//Chat GPT 답변을 담을 DTO
public class ChatGptResponseDto {
    private List<Choice> choices;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private ChatGptMessage message;

    }
}
