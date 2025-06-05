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

            if (raw.startsWith("```")) {
                int start = raw.indexOf("{");
                int end = raw.lastIndexOf("}");
                raw = (start != -1 && end != -1) ? raw.substring(start, end + 1) : "{}";
            }

            return objectMapper.readValue(raw, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("AI 공정채용 리뷰 실패", e);
            return Map.of("error", "AI 분석에 실패했습니다.");
        }
    }
}
