package com.multi.matchingbot.chatbot.util;

import com.multi.matchingbot.chatbot.domain.JobLawReviewRequest;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JobLawPromptBuilder {
    private static final String TEMPLATE = """
            너는 고용노동부 기준의 공정 채용 검토를 담당하는 AI야.
            아래 채용공고 항목들을 검토하고, 차별적 표현, 모호한 요구사항, 법령 위반 가능성이 있는지 판단해줘.
            
            분석 기준: 
            「채용절차의 공정화에 관한 법률」 (고용노동부) 
            - 법령 링크: https://www.law.go.kr/법령/채용절차의공정화에관한법률
            
            [공고 제목]: {title} 
            [설명]: {description} 
            [근무지]: {address} 
            [주요 업무]: {mainTask} 
            [필요 역량]: {requiredSkills} 
            [인재상]: {requiredTraits} 
            [안내사항]: {notice}
            
            각 항목별로 위반 소지가 있는 표현이 있다면 어떤 문제인지 구체적으로 지적해줘. 
            공정 채용 관점에서 실질적인 문제 여부에 집중해줘. 단순한 스타일 문제는 생략해도 괜찮아. 
            최종적으로는 전체 공고에 대한 종합 평가 요약도 포함해줘.
            
            응답은 반드시 아래 JSON 형식으로 정리해서 줘: 
            
            \\{
              "titleReview": "...",
              "descriptionReview": "...",
              "mainTaskReview": "...",
              "requiredSkillsReview": "...",
              "requiredTraitsReview": "...",
              "noticeReview": "...",
              "summary": "(종합 평가 요약)"
            \\}
            
            ※ JSON 코드블럭 없이, 순수 JSON 텍스트만 반환해 줘. 
            ※ 각 항목의 리뷰는 간단하되 명확하게 지적해 줘. 
            ※ 공정 채용 기준에 근거한 지적이라면, 해당 기준을 짧게 언급해도 좋아.
            """;

    public String build(JobLawReviewRequest dto) {
        PromptTemplate template = new PromptTemplate(TEMPLATE);
        Map<String, Object> input = Map.of(
                "title", dto.getTitle(),
                "description", dto.getDescription(),
                "address", dto.getAddress(),
                "mainTask", dto.getMainTask(),
                "requiredSkills", dto.getRequiredSkills(),
                "requiredTraits", dto.getRequiredTraits(),
                "notice", dto.getNotice() == null ? "" : dto.getNotice()
        );
        return template.render(input);
    }
}
