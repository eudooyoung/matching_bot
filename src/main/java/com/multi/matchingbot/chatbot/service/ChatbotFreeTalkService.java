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

    private static final int MAX_LENGTH = 500;

    public String talk(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "입력된 메시지가 없습니다. 다시 입력해 주세요.";
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ 간결한 시스템 프롬프트
        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "당신은 간결하고 핵심만 알려주는 비서입니다. 응답은 3문장 이내로 요약해 주세요."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", entity, Map.class
            );

            if (response.getBody() == null) {
                return "GPT로부터 응답을 받지 못했습니다.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null && message.get("content") != null) {
                    String result = message.get("content").toString().trim().replaceAll("\\s+", " ");

                    // ✅ 응답이 너무 길 경우 요약 문구 추가
                    if (result.length() > MAX_LENGTH) {
                        return result.substring(0, MAX_LENGTH) + "... (더 자세한 정보는 관련 링크를 참고해 주세요)";
                    }

                    return result;
                }
            }

            return "GPT로부터 유효한 응답을 받지 못했습니다.";

        } catch (Exception e) {
            e.printStackTrace();
            return "GPT 응답 처리 중 오류 발생: " + e.getMessage();
        }
    }
}
