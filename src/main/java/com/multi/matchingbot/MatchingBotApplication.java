package com.multi.matchingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MatchingBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchingBotApplication.class, args);

    }

}
