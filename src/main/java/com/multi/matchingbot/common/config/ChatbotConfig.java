package com.multi.matchingbot.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatbotConfig {

    @Bean(name = "evaluationChatClient")
    public ChatClient evaluationChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean(name = "lawReviewChatClient")
    public ChatClient lawReviewChatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
