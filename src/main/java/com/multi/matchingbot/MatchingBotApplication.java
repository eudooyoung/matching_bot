package com.multi.matchingbot;

import com.multi.matchingbot.common.config.RoleAccessProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(RoleAccessProperties.class)
public class MatchingBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchingBotApplication.class, args);

    }

}
