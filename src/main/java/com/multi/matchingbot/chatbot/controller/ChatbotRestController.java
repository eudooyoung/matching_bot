package com.multi.matchingbot.chatbot.controller;

import com.multi.matchingbot.chatbot.domain.JobLawReviewRequest;
import com.multi.matchingbot.chatbot.service.ChatbotJobReviewService;
import com.multi.matchingbot.chatbot.service.ChatbotReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotRestController {

    private final ChatbotReportService reportService;
    private final ChatbotJobReviewService reviewService;

    @PostMapping("/evaluate")
    public ResponseEntity<Map<String, Object>> generateCompanyReport(@RequestBody Map<String, Object> input) {
        Map<String, Object> report = reportService.generateReport(input);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/law-review")
    public ResponseEntity<?> reviewJobPost(@Valid @RequestBody JobLawReviewRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Map<String, Object> reviewResult = reviewService.review(request); // 실제 리뷰 결과 리턴
        return ResponseEntity.ok().body(Map.of("review", reviewResult));
    }

}
