package com.multi.matchingbot.common.config;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final RoleAccessProperties roleAccess;

    //    http://localhost:8091/swagger-ui/index.html
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("공개 API")
                .pathsToMatch(roleAccess.getPermitAll().toArray(new String[0]))
                .build();
    }

    @Bean
    public GroupedOpenApi authenticatedApi() {
        return GroupedOpenApi.builder()
                .group("인증된 사용자 API")
                .pathsToMatch(roleAccess.getAuthenticatedPaths().toArray(new String[0]))
                .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("개인회원 API")
                .pathsToMatch(roleAccess.getMemberPaths().toArray(new String[0]))
                .build();
    }

    @Bean
    public GroupedOpenApi companyApi() {
        return GroupedOpenApi.builder()
                .group("기업회원 API")
                .pathsToMatch(roleAccess.getCompanyPaths().toArray(new String[0]))
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("관리자 API")
                .pathsToMatch(roleAccess.getAdminPaths().toArray(new String[0]))
                .build();
    }
}
