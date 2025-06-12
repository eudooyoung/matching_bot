package com.multi.matchingbot.ai.resumeanalysis;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class KeywordExtractor {

    public List<String> extractKeywords(String text) {
        String url = "http://18.223.234.101:8081"; // FastAPI 서버 주소

        // form data 구성
        Map<String, String> body = new HashMap<>();
        body.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // ⚠️ FastAPI가 받는 방식

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("keywords")) {
                    return (List<String>) responseBody.get("keywords");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 로그로 오류 확인
        }

        return Collections.emptyList(); // 실패 시 빈 리스트 반환
    }
}
