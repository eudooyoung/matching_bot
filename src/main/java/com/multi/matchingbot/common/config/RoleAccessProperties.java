package com.multi.matchingbot.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.access")
public class RoleAccessProperties {
    private List<String> permitAll;
    private List<String> memberPaths;
    private List<String> companyPaths;
    private List<String> adminPaths;
    private List<String> apiPaths;
}
