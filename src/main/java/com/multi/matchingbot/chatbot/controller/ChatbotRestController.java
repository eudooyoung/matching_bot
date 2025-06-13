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
import java.util.List;
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
        System.out.println("✅ GPT 요청 메시지: " + message); // 디버깅용

        String reply = null;
        String redirectUrl = null;

        // ✅ 무의미하거나 너무 짧은 메시지 필터링
        List<String> bannedWords = List.of("서", "법", "팁", "정보");
        if (message.length() < 2 || bannedWords.contains(message)) {
            reply = "\"" + message + "\"는 너무 짧거나 모호합니다. 좀 더 자세히 입력해 주세요.";
        }

        // ✅ 사전 프롬프트 처리
        if (message.contains("기능") && message.contains("사이트")) {
            reply = "이 사이트에서는 다음과 같은 기능을 사용할 수 있어요:\n" +
                    "- 채용공고 탐색 및 위치 기반 지도 보기\n" +
                    "- 커뮤니티에서 후기 공유\n" +
                    "- 기업/개인 마이페이지 기능\n" +
                    "- AI 이력서 분석 및 기업 보고서 제공\n" +
                    "- 회원가입 없이 커뮤니티 둘러보기 가능";
        }
        // ✅ 명령어 분기 및 유사 키워드 처리
        else {
            switch (message) {
                case "개인 회원가입":
                    reply = "회원가입 페이지로 이동합니다.";
                    redirectUrl = "/member/register";
                    break;

                case "기업 회원가입":
                    reply = "회원가입 페이지로 이동합니다.";
                    redirectUrl = "/company/register";
                    break;

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

                case "채용 공고 관리":
                    if ("COMPANY".equals(role)) {
                        reply = "채용 공고 관리 페이지로 이동합니다.";
                        redirectUrl = "/job/manage-jobs";
                    } else {
                        reply = "기업회원만 접근 가능한 기능입니다.";
                    }
                    break;

                case "관심 이력서 목록":
                    if ("COMPANY".equals(role)) {
                        reply = "관심 이력서 목록 페이지로 이동합니다.";
                        redirectUrl = "/job/resume-bookmark";
                    } else {
                        reply = "기업회원만 접근 가능한 기능입니다.";
                    }
                    break;

                case "기업 정보 수정":
                    if ("COMPANY".equals(role)) {
                        reply = "기업 정보 수정 페이지로 이동합니다.";
                        redirectUrl = "/company/edit-profile";
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

                case "이력서 관리":
                    if ("MEMBER".equals(role)) {
                        reply = "이력서 관리 페이지로 이동합니다.";
                        redirectUrl = "/member";
                    } else {
                        reply = "일반 회원만 접근 가능한 기능입니다.";
                    }
                    break;

                case "관심 기업 목록":
                    if ("MEMBER".equals(role)) {
                        reply = "관심 기업 목록 페이지로 이동합니다.";
                        redirectUrl = "/member/company-bookmark";
                    } else {
                        reply = "일반 회원만 접근 가능한 기능입니다.";
                    }
                    break;

                case "관심 공고 목록":
                    if ("MEMBER".equals(role)) {
                        reply = "관심 공고 목록 페이지로 이동합니다.";
                        redirectUrl = "/member/job-bookmark";
                    } else {
                        reply = "일반 회원만 접근 가능한 기능입니다.";
                    }
                    break;

                case "개인 정보 수정":
                    if ("MEMBER".equals(role)) {
                        reply = "개인 정보 수정 페이지로 이동합니다.";
                        redirectUrl = "/member/profile_edit";
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
                    // ✅ 유사 키워드도 함께 처리
                    if (message.contains("가입") || message.contains("계정 만들") || message.contains("회원 가입")) {
                        reply = "회원가입 페이지로 이동합니다.";
                        redirectUrl = "/auth/register";
                    } else {
                        reply = freeTalkService.talk(message);
                    }
                    break;
            }
        }

        // ✅ switch/if-else 외부에서 공통 처리
        Map<String, Object> response = new HashMap<>();
        response.put("reply", reply);
        if (redirectUrl != null) {
            response.put("redirect", true);
            response.put("url", redirectUrl);
        }

        return ResponseEntity.ok(response);
    }
}
