package com.multi.matchingbot.member.controller;


import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.resume.domain.dto.ResumeDto;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeApiController {

    private final ResumeService resumeService;
    private final JobService jobService;
    private final RestTemplate restTemplate;

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

    // 0610
    @PostMapping("/sort-resumes-by-job")
    public ResponseEntity<List<ResumeDto>> sortResumesByJob(@RequestBody Map<String, Object> payload) {
        Long jobId = Long.valueOf(payload.get("jobId").toString());

        Job job = jobService.findById(jobId);
        List<String> jobSkillKeys = Arrays.asList(job.getSkillKeywords().split(","));
        List<String> jobTraitKeys = Arrays.asList(job.getTraitKeywords().split(","));

        List<Resume> allResumes = resumeService.findAll();
        List<Map<String, Object>> resumePayload = allResumes.stream().map(resume -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", resume.getId());
            map.put("skill_keywords", Arrays.asList(resume.getSkillKeywords().split(",")));
            map.put("trait_keywords", Arrays.asList(resume.getTraitKeywords().split(",")));
            return map;
        }).collect(Collectors.toList());

        Map<String, Object> requestToFastAPI = new HashMap<>();
        requestToFastAPI.put("job_skill_keys", jobSkillKeys);
        requestToFastAPI.put("job_trait_keys", jobTraitKeys);
        requestToFastAPI.put("resume_list", resumePayload);

        ResponseEntity<List<Map<String, Object>>> fastApiResponse = restTemplate.exchange(
                "http://localhost:8081/sort-resumes-by-job",
                HttpMethod.POST,
                new HttpEntity<>(requestToFastAPI),
                new ParameterizedTypeReference<>() {}
        );

        List<Long> sortedResumeIds = fastApiResponse.getBody().stream()
                .map(e -> ((Number) e.get("resume_id")).longValue())
                .collect(Collectors.toList());

        // 점수 매핑
        Map<Long, Double> resumeScoreMap = new HashMap<>();
        for (Map<String, Object> map : fastApiResponse.getBody()) {
            Long resumeId = ((Number) map.get("resume_id")).longValue();
            Double score = map.get("score") instanceof Integer
                    ? ((Integer) map.get("score")).doubleValue()
                    : (Double) map.get("score");
            resumeScoreMap.put(resumeId, score);
        }

        List<ResumeDto> sortedResumes = resumeService.findByIdsPreserveOrder(sortedResumeIds);
        for (ResumeDto dto : sortedResumes) {
            dto.setSimilarityScore(resumeScoreMap.get(dto.getId()));
        }

        return ResponseEntity.ok(sortedResumes);
    }
}

