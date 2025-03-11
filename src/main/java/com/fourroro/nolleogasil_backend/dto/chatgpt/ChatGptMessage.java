package com.fourroro.nolleogasil_backend.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * ChatGpt API에 요청을 보낼 떄의 메세지 형태를 나타내는 클래스입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptMessage {
    private String role;
    private String content;

}
