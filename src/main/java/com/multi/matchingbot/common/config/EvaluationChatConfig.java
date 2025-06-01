package com.multi.matchingbot.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EvaluationChatConfig {

    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    @Bean(name = "evaluationChatClient")
    @ConditionalOnProperty(name = "spring.ai.openai.api-key")
    public ChatClient evaluationChatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
