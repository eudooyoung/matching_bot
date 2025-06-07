package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateDto {

    private Long id;
    private String email;
    private String name;
    private String businessNo;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Size(max = 20)
    private String phone;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    @Size(max = 200)
    private String address;

    @NotBlank(message = "산업군은 필수 입력값입니다.")
    @Size(max = 50)
    private String industry;

    @NotNull(message = "직원 수를 입력해주세요.")
    @Min(value = 0, message = "0명 이상 입력해주세요.")
    private Integer headcount;

    @NotNull(message = "연 매출을 입력해주세요.")
    @Min(value = 0, message = "0 이상 입력해주세요.")
    private Integer annualRevenue;

    @NotNull(message = "영업 이익을 입력해주세요.")
    private Integer operatingIncome;

    @NotNull(message = "작년 채용 공고 수를 입력해주세요.")
    @Min(value = 0, message = "0 이상 입력해주세요.")
    private Integer jobsLastYear;

    private Yn agreeTerms;
    private Yn agreePrivacy;
    private Yn agreeFinance;
    private Yn agreeMarketing;
    private Yn agreeThirdParty;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public CompanyUpdateDto(Long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
