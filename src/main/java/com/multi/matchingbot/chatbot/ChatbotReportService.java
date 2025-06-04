package com.multi.matchingbot.chatbot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotReportService {

    @Qualifier("evaluationChatClient")
    private final ChatClient evaluationClient;

    private final TemplateEngine templateEngine;

    // text 생성
    private static final String TEMPLATE = """
            너는 전문적인 기업 재무 분석가이자 브랜딩 문구 작성에도 능한 카피라이터야.
            다음 정보를 바탕으로 외부 공개용 기업 평가 리포트를 작성해 줘.
            
            - 말투는 부드러운 존댓말을 사용해. (~입니다 / ~보입니다 / ~기대됩니다 등)
            - 단정적이거나 부정적인 표현은 피해줘.
            - 부족한 점은 완곡하게 표현하고, 긍정적 가능성을 함께 제시해 줘.
            - 기업 홈페이지에 노출될 내용을 작성하는 거니까, 자연스럽고 브랜드 이미지에 도움이 되도록 표현해 줘. 주로 구직자들이 열람하게 될 내용이야.
            - 기업 평판 항목은 입력된 기업 정보(설립연도, 채용 활동, 재무 지표 등)를 기반으로 추정 가능한 범위에서 작성해.
            - 외부 데이터가 없거나 특정 정보가 누락된 경우, ‘제공된 정보 기준으로 보았을 때~’, ‘업계 기대에 부합하는 모습으로 보입니다’ 등의 표현을 활용해 완곡하게 설명해 줘.
            - 만약 부채 정보가 제공되지 않았다면, 해당 항목은 생략하거나 언급하지 않아도 괜찮아.
            
            입력 정보:
            - 회사명: {name}
            - 설립연도: {year}
            - 직원 수: {headcount}명
            - 산업군: {industry}
            - 연매출: {revenue}억 원
            - 영업이익: {income}억 원
            - 최근 1년간 채용공고 수: {jobs}
            {debt_note}
            
            분석 기준은 아래 4가지 항목을 중심으로 하고, 각 항목별 점수(5점 만점), 간단한 요약, 핵심 의견을 제시해:
            - 수익성 (Profitability)
            - 성장성 (Growth Potential)
            - 안정성 (Stability)
            - 기업 평판 (Reputation)
            
            마지막에는 종합 요약 및 AI의 전체 의견도 포함해 줘.
            
            응답 형식은 반드시 다음 JSON 구조를 줘야해:
            \\{
              "summary": "(한 줄 기업 소개, 긍정적이고 자연스럽게)",
              "description": "(기업 특징 서술, 존댓말)",
              "financeSummary": "...",
              "employmentSummary": "...",
              "totalScore": "수치 (예: 4.3)",
              "scoreFinance": "수치",
              "scoreEmployment": "수치",
              "scoreGrowth": "수치",
              "scoreReputation": "수치",
              "noteFinance": "...",
              "noteEmployment": "...",
              "noteGrowth": "...",
              "noteReputation": "...",
              "opinion": "(종합 평가 의견, 300자 내외)",
              "keywords":  ["핵심 키워드1", "핵심 키워드2", "핵심 키워드3"]
            \\}
            
            ※ summary, description, financeSummary, employmentSummary는 한 문장으로 부드럽게 작성해 줘. 
            ※ 점수는 문자열로, 소수점 한 자리까지 적어줘. (예: "4.3") 
            ※ 키워드는 3~5개 정도, 기업의 강점 중심으로 작성해 줘. 
            ※ 응답은 반드시 JSON 객체만 반환하고, 코드 블록은 없어야 해.
            """;

    /**
     * AI 리포트 텍스트 생성 및 파싱
     *
     * @param input 기업 기초 정보
     * @return 분석 정보가 포함된 Map (JSON 파싱 결과)
     */
    public Map<String, Object> generateReport(Map<String, Object> input) {
        try {
            // 프롬프트 템플릿 생성
            PromptTemplate template = new PromptTemplate(TEMPLATE);
            // 프롬프트 템플릿 렌더링
            String prompt = template.render(input);

            // 응답 초기값
            String aiResponse = evaluationClient
                    .prompt(prompt)
                    .call()
                    .content();

            if (aiResponse == null || aiResponse.trim().isEmpty()) {
                log.warn("!! AI 응답 없음");
                return new HashMap<>();
            }

            // 전처리 -> json형식 아닌 부분 삭제
            String raw = aiResponse.trim();

            if (raw.startsWith("```")) {
                int start = raw.indexOf("{");
                int end = raw.lastIndexOf("}");
                if (start != -1 && end != -1) {
                    raw = raw.substring(start, end + 1);
                } else {
                    log.warn("!! Ai 응답 Json 형식 오류");
                    return new HashMap<>();
                }
            }

            // json -> map parsing
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(raw, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("!! AI 리포트 생성 실패: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }


    // 리팩토링 완료
   /* // 이미지 변환
    public File convertReportToImage(Map<String, Object> reportData, String companyId) throws Exception {
        // html 렌더링
        Context context = new Context();
        context.setVariables(reportData);
        String html = templateEngine.process("chatbot/evaluation-report", context);

        // html -> pdf
        ByteArrayOutputStream pdfOutPut = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        File fontFile = new File(getClass().getClassLoader().getResource("fonts/NanumGothic.ttf").toURI());
        System.out.println("✅ 폰트 파일 존재? " + fontFile.exists());
        System.out.println("✅ 경로: " + fontFile.getAbsolutePath());
        builder.useFont(fontFile, "NanumGothic");
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(pdfOutPut);
        builder.run();

        // pdf -> image
        BufferedImage image;
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfOutPut.toByteArray()))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage fullImage = renderer.renderImageWithDPI(0, 200);  // 화질 제어
            log.info("PDF 페이지 높이: {}px", fullImage.getHeight());
            log.info("PDF 페이지 너비: {}px", fullImage.getWidth());

            // ✅ 위쪽, 아래쪽 잘라내기
            int cropTop = 60;
            int cropBottom = 100;
            int croppedHeight = fullImage.getHeight() - cropTop - cropBottom;

            image = fullImage.getSubimage(0, cropTop, fullImage.getWidth(), croppedHeight);
//            image = fullImage;
        }

        // 이미지 저장
        String relativePath = "/upload/report/";
        String baseName = "report-" + companyId;
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = ".png";

        String originalName = baseName + extension;
        String systemName = baseName + "-" + uuid + extension;

        String basePath = new File("src/main/resources/static" + relativePath).getAbsolutePath();
        new File(basePath).mkdirs();

        File output = new File(basePath, systemName);
        ImageIO.write(image, "png", output);
        log.info("이미지 생성 성공");
        return output;
    }*/

    /*public File generateFullReportImage(CompanyRegisterDto dto, Long companyId) {
        try {
            Map<String, Object> reportData = ReportDataBuilder.fromCompany(dto);

            // 리포트 생성
            String aiResponse = generateReport(reportData);

            // 응답 유효성 검사
            String raw = aiResponse != null ? aiResponse.trim() : null;
            if (raw == null || raw.isBlank()) {
                log.warn("!! AI 응답 비어있음");
                return null;
            }

            if (raw.startsWith("```")) {
                int start = raw.indexOf("{");
                int end = raw.lastIndexOf("}");
                if (start != -1 && end != -1) {
                    raw = raw.substring(start, end + 1);
                } else {
                    log.warn("!! Ai응답 Json 형식 오류");
                    return null;
                }
            }

            // json -> map parsing
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> parsed = objectMapper.readValue(raw, new TypeReference<>() {
            });
            reportData.putAll(parsed);
            return convertReportToImage(reportData, String.valueOf(companyId));
        } catch (Exception e) {
            System.err.println("AI 평가 이미지 생성 실패: " + e.getMessage());
            return null;
        }
    }*/


}
