package com.multi.matchingbot.chatbot.controller;

import com.multi.matchingbot.chatbot.domain.JobLawReviewRequest;
import com.multi.matchingbot.chatbot.service.ChatbotFreeTalkService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chatbot")
public class ChatbotRestController {

    private final ChatbotReportService reportService;
    private final ChatbotJobReviewService reviewService;
    private final ChatbotFreeTalkService freeTalkService;
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
        Map<String, Object> reviewResult = reviewService.review(request);
        return ResponseEntity.ok().body(Map.of("review", reviewResult));
    }

    @PostMapping("/talk")
    public ResponseEntity<?> talkWithGpt(@RequestBody Map<String, String> request) {
        System.out.println("✅ talkWithGpt 호출됨: " + request);
        String message = request.get("message");
        String reply = freeTalkService.talk(message);

        if (reply == null) {
            reply = "죄송합니다. 답변을 생성하지 못했습니다.";
        }

        return ResponseEntity.ok().body(Map.of("reply", reply));
    }



//    @PostConstruct
//    public void init() {
//        System.out.println("✅ ChatbotRestController initialized");
//    }

}
