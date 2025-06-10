package com.multi.matchingbot.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ChatbotFreeTalkService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    public String talk(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "입력된 메시지가 없습니다. 다시 입력해 주세요.";
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", entity, Map.class
            );

            System.out.println("🧠 GPT 응답 수신: " + response.getBody());

            if (response.getBody() == null) {
                return "GPT로부터 응답을 받지 못했습니다.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null && message.get("content") != null) {
                    return message.get("content").toString().trim();
                }
            }

            return "GPT로부터 유효한 응답을 받지 못했습니다.";

        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에서 예외 내용 확인
            return "GPT 응답 처리 중 오류 발생: " + e.getMessage();
        }
    }
}
