package com.multi.matchingbot.common.controller;

import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.service.ResumeService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/main")
public class MainApiController {

    private final ResumeService resumeService;
    private final JobService jobService;
    private final RestTemplate restTemplate;

    public MainApiController(ResumeService resumeService, JobService jobService, RestTemplate restTemplate) {
        this.resumeService = resumeService;
        this.jobService = jobService;
        this.restTemplate = restTemplate;
    }

    /*@GetMapping("/{id}/keywords")
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
    }*/

    // 0609
    @PostMapping("/sort-jobs-by-resume")
    public ResponseEntity<List<JobDto>> sortJobsByResume(@RequestBody Map<String, Object> payload) {
        Long resumeId = Long.valueOf(payload.get("resumeId").toString());

        Resume resume = resumeService.findById(resumeId);
        List<String> resumeSkillKeys = Arrays.asList(resume.getSkillKeywords().split(","));
        List<String> resumeTraitKeys = Arrays.asList(resume.getTraitKeywords().split(","));

        List<Job> allJobs = jobService.findAll();
        List<Map<String, Object>> jobPayload = allJobs.stream().map(job -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", job.getId());
            map.put("skill_keywords", Arrays.asList(job.getSkillKeywords().split(",")));
            map.put("trait_keywords", Arrays.asList(job.getTraitKeywords().split(",")));
            return map;
        }).collect(Collectors.toList());

        Map<String, Object> requestToFastAPI = new HashMap<>();
        requestToFastAPI.put("resume_skill_keys", resumeSkillKeys);
        requestToFastAPI.put("resume_trait_keys", resumeTraitKeys);
        requestToFastAPI.put("job_list", jobPayload);

        ResponseEntity<List<Map<String, Object>>> fastApiResponse = restTemplate.exchange(
                "http://localhost:8081/sort-jobs-by-resume",
                HttpMethod.POST,
                new HttpEntity<>(requestToFastAPI),
                new ParameterizedTypeReference<>() {}
        );

        List<Long> sortedJobIds = fastApiResponse.getBody().stream()
                .map(e -> ((Number) e.get("job_id")).longValue())
                .collect(Collectors.toList());

        // 점수 매핑
        Map<Long, Double> jobScoreMap = new HashMap<>();
        for (Map<String, Object> map : fastApiResponse.getBody()) {
            Long jobId = ((Number) map.get("job_id")).longValue();
            Double score = map.get("score") instanceof Integer
                    ? ((Integer) map.get("score")).doubleValue()
                    : (Double) map.get("score");
            jobScoreMap.put(jobId, score);
        }

        List<JobDto> sortedJobs = jobService.findByIdsPreserveOrder(sortedJobIds);
        for (JobDto dto : sortedJobs) {
            dto.setSimilarityScore(jobScoreMap.get(dto.getId()));
        }

        return ResponseEntity.ok(sortedJobs);
    }

}