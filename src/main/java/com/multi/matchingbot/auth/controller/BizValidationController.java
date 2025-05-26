package com.multi.matchingbot.auth.controller;

import com.multi.matchingbot.common.config.ApiKeyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BizValidationController {

    private final ApiKeyProvider apiKeyProvider;

    @PostMapping("/validate-business")
    public ResponseEntity<String> validateBusiness(@RequestBody Map<String, Object> payload) {
        String apiKey = apiKeyProvider.getOdCloudKey();
        String url = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}
