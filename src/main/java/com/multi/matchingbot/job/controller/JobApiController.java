package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApiController {

    private final JobService jobService;
    private final RestTemplate restTemplate = new RestTemplate();

    // 공고 삭제하기
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") Long id) {
        jobService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/keywords")
    public ResponseEntity<Map<String, Object>> getJobKeywords(@PathVariable("id") Long id) {
        Job job = jobService.findById(id);  // jobId로 조회
        System.out.println(job);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Job not found"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("skillKeywords", parseKeywords(job.getSkillKeywords()));
        result.put("traitKeywords", parseKeywords(job.getTraitKeywords()));
        System.out.println(result);
        return ResponseEntity.ok(result);
    }

    // 문자열 키워드를 쉼표 기준으로 분리하는 유틸
    private List<String> parseKeywords(String keywordString) {
        if (keywordString == null || keywordString.trim().isEmpty()) return List.of();
        return Arrays.stream(keywordString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }


    @PostMapping("/similarity")
    public ResponseEntity<Map<String, Object>> calculateSimilarity(@RequestBody Map<String, List<String>> body) {
        // 1. 필요한 값 추출
        List<String> resumeSkills = body.getOrDefault("resume_skill_keys", List.of());
        List<String> resumeTraits = body.getOrDefault("resume_trait_keys", List.of());
        List<String> jobSkills = body.getOrDefault("job_skill_keys", List.of());
        List<String> jobTraits = body.getOrDefault("job_trait_keys", List.of());

        // 2. FastAPI 호출을 위한 요청 객체 생성
        Map<String, Object> request = Map.of(
                "resume_skill_keys", resumeSkills,
                "resume_trait_keys", resumeTraits,
                "job_skill_keys", jobSkills,
                "job_trait_keys", jobTraits
        );

        // 3. FastAPI에 POST 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://18.223.234.101:8081/calculate-similarity", request, Map.class
        );

        // 4. 응답 그대로 전달
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/extract")
    public ResponseEntity<?> extractKeywords(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "텍스트가 비어 있습니다."));
        }

        // FastAPI에 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("text", text), headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://18.223.234.101:8081/extract", request, Map.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            log.error("FastAPI 호출 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "키워드 추출 실패"));
        }
    }
}