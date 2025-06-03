package com.multi.matchingbot.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EvaluationChatConfig {

    @Bean(name = "evaluationChatClient")
    public ChatClient evaluationChatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
