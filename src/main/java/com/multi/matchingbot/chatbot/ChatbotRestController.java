package com.multi.matchingbot.chatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotRestController {

    private final ChatbotReportService chatbotReportService;

    @PostMapping("/evaluate")
    public ResponseEntity<Map<String, Object>> generateCompanyReport(@RequestBody Map<String, Object> input) {
        Map<String, Object> report = chatbotReportService.generateReport(input);
        return ResponseEntity.ok(report);
    }
}
