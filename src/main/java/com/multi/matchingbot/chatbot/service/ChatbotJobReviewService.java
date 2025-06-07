package com.multi.matchingbot.chatbot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.matchingbot.chatbot.domain.JobLawReviewRequest;
import com.multi.matchingbot.chatbot.util.JobLawPromptBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ChatbotJobReviewService {

   /* @Qualifier("lawReviewChatClient")
    private final ChatClient chatClient;

    private final JobLawPromptBuilder promptBuilder;
    private final ObjectMapper objectMapper;*/

    private final ChatClient chatClient;
    private final JobLawPromptBuilder promptBuilder;
    private final ObjectMapper objectMapper;

    public ChatbotJobReviewService(
            @Qualifier("lawReviewChatClient") ChatClient chatClient,
            JobLawPromptBuilder promptBuilder,
            ObjectMapper objectMapper
    ) {
        this.chatClient = chatClient;
        this.promptBuilder = promptBuilder;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> review(JobLawReviewRequest request) {
        try {
            String prompt = promptBuilder.build(request);
            String aiResponse = chatClient.prompt().user(prompt).call().content();
            String raw = aiResponse != null ? aiResponse.trim() : "";

            // GPT 응답 전처리
            if (raw.contains("```")) {
                raw = raw.replaceAll("(?s)```(json)?", "").trim();
            }

            // 앞에 설명 텍스트 제거
            int braceIndex = raw.indexOf("{");
            if (braceIndex > 0) {
                raw = raw.substring(braceIndex);
            }

            return objectMapper.readValue(raw, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("AI 공정채용 리뷰 실패", e);
            return Map.of("error", "AI 분석에 실패했습니다.");
        }
    }
}
