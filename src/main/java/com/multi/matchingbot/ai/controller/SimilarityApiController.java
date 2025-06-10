/*
package com.multi.matchingbot.ai.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/similarity")
public class SimilarityApiController {

    private final JobService jobService;
    private final ResumeService resumeService;

    public SimilarityApiController(JobService jobService, ResumeService resumeService) {
        this.jobService = jobService;
        this.resumeService = resumeService;
    }

    @GetMapping("/job/{jobId}")
    public String showJobDetail(@PathVariable Long jobId,
                                @RequestParam(required = false) Long resumeId,
                                @AuthenticationPrincipal MBotUserDetails userDetails,
                                Model model) {
        Job job = jobService.findById(jobId);
        model.addAttribute("job", job);
        model.addAttribute("role", userDetails.getRole());
        model.addAttribute("companyId", job.getCompany().getId());

        // 전체 이력서 목록
        if (userDetails != null && userDetails.getRole().equals("MEMBER")) {
            List<Resume> resumes = resumeService.findByMemberId(userDetails.getMemberId());
            model.addAttribute("resumes", resumes);
        }

        // 선택된 resume가 있으면 그 resume도 model에 추가
        if (resumeId != null) {
            Resume selectedResume = resumeService.findById(resumeId);
            model.addAttribute("resume", selectedResume);

        }

        return "job-detail";
    }

    @GetMapping("/job/{id}/similarity")
    @ResponseBody
    public ResponseEntity<Double> calculateSimilarity(
            @PathVariable Long id,
            @AuthenticationPrincipal MBotUserDetails userDetails) {

        Job job = jobService.findById(id);
        List<Resume> resumeList = resumeService.findByMemberId(userDetails.getMemberId());
        Resume resume = resumeList.isEmpty() ? null : resumeList.get(0);

        List<String> jobKeywords = Arrays.stream(
                        Optional.ofNullable(job)
                                .map(Job::getSkillKeywords)
                                .orElse("")
                                .split(",")
                ).map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();


        List<String> resumeKeywords = Arrays.stream(
                        Optional.ofNullable(resume)
                                .map(Resume::getSkillKeywords)
                                .orElse("")
                                .split(",")
                ).map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        Map<String, Object> body = Map.of(
                "job_skill_keys", jobKeywords,
                "resume_skill_keys", resumeKeywords
        );
        System.out.println("jobKeywords"+jobKeywords);
        System.out.println("resumeKeywords"+resumeKeywords);

        try {
            RestTemplate restTemplate = new RestTemplate();
            Double similarity = restTemplate.postForObject(
                    "http://localhost:8081/calculate-similarity", body, Double.class
            );
            return ResponseEntity.ok(similarity);
        } catch (Exception e) {
            System.out.println("⚠️ Python 서버 요청 실패: " + e.getMessage());
            return ResponseEntity.badRequest().build(); // 또는 500
        }

    }
}*/
