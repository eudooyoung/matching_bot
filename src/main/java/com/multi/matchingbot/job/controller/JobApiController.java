package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApiController {

    private final JobService jobService;

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
}