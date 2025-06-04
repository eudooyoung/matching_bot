package com.multi.matchingbot.company.domain;

import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CompanyRegisterDto {
    //validation 처리

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "회사명을 입력하세요.")
    private String name;

    @NotBlank(message = "사업자등록번호를 입력하세요.")
    @Pattern(regexp = "\\d{10}", message = "사업자등록번호는 숫자 10자리여야 합니다.")
    private String businessNo;

    @NotBlank(message = "주소를 입력하세요.")
    private String address;

    @NotBlank(message = "산업군을 입력하세요.")
    private String industry;

    @Min(value = 1900, message = "설립연도는 1900년 이후여야 합니다.")
    @Max(value = 2100, message = "설립연도는 2100년 이전이어야 합니다.")
    private int establishedYear;

    @Min(value = 1, message = "직원 수는 1명 이상이어야 합니다.")
    private int headcount;

    @Min(value = 0, message = "연매출은 0 이상이어야 합니다.")
    private int annualRevenue;

    @Min(value = 0, message = "영업이익은 0 이상이어야 합니다.")
    private int operatingIncome;

//    @Column(name = "jobs_last_year")
    @Min(value = 0, message = "작년 채용 수는 0 이상이어야 합니다.")
    private int jobsLastYear;

    @NotNull(message = "이용약관 동의는 필수입니다.")
    private Yn agreeTerms;

    @NotNull(message = "개인정보 처리방침 동의는 필수입니다.")
    private Yn agreePrivacy;

    private Yn agreeFinance;
    private Yn agreeMarketing;
    private Yn agreeThirdParty;
}