package com.multi.matchingbot.ai.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/similarity")
public class SimilarityController {
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/calculate-similarity")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calculateSimilarity(@RequestBody Map<String, List<String>> payload) {
        try {
            // FastAPI 서버 주소
            String url = "http://localhost:8081/calculate-similarity";

            // FastAPI로 POST 요청 전송
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            // 결과 반환
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "유사도 계산 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
