/*
package com.multi.matchingbot.ai.controller;

import com.multi.matchingbot.common.security.MBotUserDetails;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.domain.entity.Resume;
import com.multi.matchingbot.member.service.ResumeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/v1/similarity")
public class SimilarityController {
    private final ResumeService resumeService;
    private final JobService jobService;

    public SimilarityController(ResumeService resumeService, JobService jobService) {
        this.resumeService = resumeService;
        this.jobService = jobService;
    }

    */
/*@GetMapping("/job/{jobId}")
    public String showResumeList(@PathVariable Long jobId,
                                 @AuthenticationPrincipal MBotUserDetails userDetails,
                                 Model model) {
        Long memberId = userDetails.getMemberId();

        // 선택된 resume
        Resume selectedResume = resumeService.findById(resumeId);
        model.addAttribute("resume", selectedResume);
        System.out.println("선택된 이력서: " + selectedResume);
        // 전체 resume 리스트
        List<Resume> resumes = resumeService.findByMemberId(memberId);
        model.addAttribute("resumes", resumes);
        System.out.println("전체 이력서: " + resumes);
        // 뷰에서 role 조건 분기용
        model.addAttribute("role", userDetails.getRole());

        return "job-detail";
    }*//*


    @GetMapping("/job/{jobId}")
    public String showResumeList(@PathVariable Long jobId,
                                 @AuthenticationPrincipal MBotUserDetails userDetails,
                                 Model model) {
        Long memberId = userDetails.getMemberId();
        String role = String.valueOf(userDetails.getRole());
        System.out.println("memberId: " + memberId);
        System.out.println("role: " + role);

        // 공고 정보
        Job job = jobService.findById(jobId);
        model.addAttribute("job", job);
        model.addAttribute("companyId", job.getCompany().getId());
        model.addAttribute("role", role);

        // MEMBER일 경우 이력서 목록 추가
        if ("MEMBER".equals(role)) {
            List<Resume> resumes = resumeService.findByMemberId(memberId);
            model.addAttribute("resumes", resumes);
            if (!resumes.isEmpty()) {
                // 초기에는 resume 선택 안 되어 있으므로 selectedResume은 생략 가능
                // model.addAttribute("resume", resumes.get(0)); <- 선택 이후 별도로 처리
            }
        }

        return "job-detail";
    }



    */
/*@PostMapping("/calculate-similarity")
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
    }*//*



}*/
