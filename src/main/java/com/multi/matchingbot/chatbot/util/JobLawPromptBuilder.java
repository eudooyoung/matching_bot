package com.multi.matchingbot.chatbot.util;

import com.multi.matchingbot.chatbot.domain.JobLawReviewRequest;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JobLawPromptBuilder {
    private static final String TEMPLATE = """
            너는 고용노동부 기준의 공정 채용 검토를 담당하는 AI야.
            
            「채용절차의 공정화에 관한 법률」 (고용노동부)  
             - 링크: https://www.law.go.kr/법령/채용절차의공정화에관한법률
             
            각 항목을 링크로 제공된 사이트에 나오는 법령을 참고하여 꼼꼼하게 검토한 뒤 적절한 수정본을 제시해줘.
            
            [공고 제목]: {title}  
            [설명]: {description}  
            [근무지]: {address}  
            [주요 업무]: {mainTask}  
            [필요 역량]: {requiredSkills}  
            [인재상]: {requiredTraits}  
            [안내사항]: {notice}
            
            ---
            
            응답은 JSON 형식으로 줘.
            각 키는 현제 컬럼으로 해주고 value 에 수정본을 넣어서 줘.
            
            ※ 응답은 반드시 코드블럭 없이, 순수 JSON 텍스트로만 줘.  
            ※ 실제 사용자가 입력할 수 있는 자연스러운 문장으로 작성해줘.  
            ※ 구체적인 컬럼에 적용할 수 없는 내용은 안내사항에 포함 시켜 줘.
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
