package com.multi.matchingbot.member.controller;

import com.multi.matchingbot.member.domain.entity.Resume;
import com.multi.matchingbot.member.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeApiController {

    private final ResumeService resumeService;

    public ResumeApiController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/{id}/keywords")
    public ResponseEntity<Map<String, Object>> getResumeKeywords(@PathVariable("id") Long id) {
        Resume resume = resumeService.findById(id);  // resumeId로 조회
        System.out.println(resume);
        if (resume == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Resume not found"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("skillKeywords", parseKeywords(resume.getSkillKeywords()));
        result.put("traitKeywords", parseKeywords(resume.getTraitKeywords()));
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

