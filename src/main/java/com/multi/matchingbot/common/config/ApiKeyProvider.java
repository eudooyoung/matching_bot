package com.multi.matchingbot.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyProvider {

    @Value("${od.cloud.key}")
    private String odCloudKey;

    public String getOdCloudKey() {
        return odCloudKey;
    }
}