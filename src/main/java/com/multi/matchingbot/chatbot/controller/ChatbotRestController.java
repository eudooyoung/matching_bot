package com.multi.matchingbot.chatbot.controller;

import com.multi.matchingbot.chatbot.domain.JobLawReviewRequest;
import com.multi.matchingbot.chatbot.service.ChatbotFreeTalkService;
import com.multi.matchingbot.chatbot.service.ChatbotJobReviewService;
import com.multi.matchingbot.chatbot.service.ChatbotReportService;
import com.multi.matchingbot.common.security.MBotUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

        Map<String, Object> reviewResult = reviewService.review(request); // 실제 리뷰 결과 리턴
        for (Map.Entry<String, Object> entry : reviewResult.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }
        return ResponseEntity.ok().body(reviewResult);
    }
//    @PostMapping("/talk")
//    public ResponseEntity<?> talkWithGpt(@RequestBody Map<String, String> request) {
//        System.out.println("✅ talkWithGpt 호출됨: " + request);
//        String message = request.get("message");
//        String reply = freeTalkService.talk(message);
//
//        if (reply == null) {
//            reply = "죄송합니다. 답변을 생성하지 못했습니다.";
//        }
//
//        return ResponseEntity.ok().body(Map.of("reply", reply));
//    }


    @PostMapping("/talk")
    public ResponseEntity<?> talkWithGpt(@RequestBody Map<String, String> request,
                                         @AuthenticationPrincipal MBotUserDetails user) {
        String message = request.get("message").trim().toLowerCase();
        String role = (user != null) ? user.getRole().name() : "GUEST";

        if ("GUEST".equals(role)) {
            Map<String, Object> guestResponse = new HashMap<>();
            guestResponse.put("reply", "로그인이 필요합니다. 로그인 페이지로 이동합니다.");
            guestResponse.put("redirect", true);
            guestResponse.put("url", "/auth/login");
            return ResponseEntity.ok(guestResponse);
        }

        String reply = null;
        String redirectUrl = null;

        switch (message) {
            case "커뮤니티":
                reply = "커뮤니티로 이동합니다.";
                redirectUrl = "/community/list";
                break;

            case "기업페이지":
                if ("COMPANY".equals(role)) {
                    reply = "기업 마이페이지로 이동합니다.";
                    redirectUrl = "/company/mypage";
                } else {
                    reply = "기업회원만 접근 가능한 기능입니다.";
                }
                break;

            case "내주변채용공고":
                if ("MEMBER".equals(role)) {
                    reply = "내 주변 채용공고로 이동합니다.";
                    redirectUrl = "/map_popup";
                } else {
                    reply = "일반 회원만 접근 가능한 기능입니다.";
                }
                break;

            case "마이페이지":
                if ("MEMBER".equals(role)) {
                    reply = "마이페이지로 이동합니다.";
                    redirectUrl = "/member/mypage";
                } else {
                    reply = "일반 회원만 접근 가능한 기능입니다.";
                }
                break;

            default:
                reply = freeTalkService.talk(message);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("reply", reply);
        if (redirectUrl != null) {
            response.put("redirect", true);
            response.put("url", redirectUrl);
        }

        return ResponseEntity.ok(response);
    }

//    @PostConstruct
//    public void init() {
//        System.out.println("✅ ChatbotRestController initialized");
//    }

}
