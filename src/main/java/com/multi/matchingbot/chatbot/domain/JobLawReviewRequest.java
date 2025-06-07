package com.multi.matchingbot.chatbot.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobLawReviewRequest {
    @NotBlank(message = "공고 제목은 필수입니다.")
    @Size(max = 100, message = "공고 제목은 100자 이내로 작성해 주세요.")
    private String title;

    @NotBlank(message = "공고 설명은 필수입니다.")
    @Size(max = 500, message = "공고 설명은 500자 이내로 작성해 주세요.")
    private String description;

    @NotBlank(message = "근무지는 필수입니다.")
    @Size(max = 100, message = "주소는 100자 이내로 작성해 주세요.")
    private String address;

    @NotBlank(message = "주요 업무는 필수입니다.")
    @Size(max = 500, message = "주요 업무는 500자 이내로 작성해 주세요.")
    private String mainTask;

    @NotBlank(message = "필요 역량을 입력해 주세요.")
    @Size(max = 500, message = "필요 역량은 500자 이내로 작성해 주세요.")
    private String requiredSkills;

    @NotBlank(message = "인재상을 입력해 주세요.")
    @Size(max = 500, message = "인재상은 500자 이내로 작성해 주세요.")
    private String requiredTraits;

    @Size(max = 255, message = "안내사항은 255자 이내로 작성해 주세요.")
    private String notice;
}
