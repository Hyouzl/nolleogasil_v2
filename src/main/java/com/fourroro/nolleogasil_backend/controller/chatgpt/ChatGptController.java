package com.fourroro.nolleogasil_backend.controller.chatgpt;


import com.fourroro.nolleogasil_backend.dto.chatgpt.ChatGptRequestDto;
import com.fourroro.nolleogasil_backend.dto.chatgpt.ChatGptResponseDto;
import com.fourroro.nolleogasil_backend.dto.travelpath.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bot")
public class ChatGptController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl(apiURL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestParam(name = "prompt") String prompt) {
        try {
            ChatGptRequestDto requestDto = new ChatGptRequestDto(model, prompt);

            ChatGptResponseDto chatGptResponseDto = webClient.post()
                    .bodyValue(requestDto)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> Mono.error(new RuntimeException("ChatGPT API 요청 실패 (클라이언트 오류)")))
                    .onStatus(status -> status.is5xxServerError(), response -> Mono.error(new RuntimeException("ChatGPT API 요청 실패 (서버 오류)")))
                    .bodyToMono(ChatGptResponseDto.class)
                    .block(); // 동기 요청


            if (chatGptResponseDto == null || chatGptResponseDto.getChoices().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ChatGPT에서 응답을 받지 못했습니다.");
            }

            ResultDto resultDto = getResultDto(chatGptResponseDto);
            return ResponseEntity.status(HttpStatus.OK).body(resultDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ChatGPT API 호출 중 오류 발생: " + e.getMessage());
        }
    }

    private static ResultDto getResultDto(ChatGptResponseDto chatGptResponseDto) {
        String responseText = chatGptResponseDto.getChoices().get(0).getMessage().getContent();
        responseText = responseText.replaceAll("\n-", "");

        // 추출한 날짜와 내용을 담을 리스트 생성
        List<String> dates = new ArrayList<>();
        List<String> infos = new ArrayList<>();

        // 날짜와 내용을 각각의 리스트에 add
        String[] lines = responseText.split("\n\n");

        for (String line : lines) {
            String[] parts = line.split("\n", 2);
            if (parts.length == 2) {
                dates.add(parts[0]);
                infos.add(parts[1]);
            }
        }

        ResultDto resultDto = new ResultDto(dates, infos);
        return resultDto;
    }
}
