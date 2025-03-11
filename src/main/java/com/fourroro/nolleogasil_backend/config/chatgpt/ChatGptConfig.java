package com.fourroro.nolleogasil_backend.config.chatgpt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 이 클래스는 ChatGpt API와 연동을 위한 Configuration class입니다.
 * @author 전선민
 * @since 2024-01-10
 */
@Configuration
public class ChatGptConfig {
    @Value("${openai.api.key}")
    private String openAiKey;
    @Bean
    public RestTemplate template(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
