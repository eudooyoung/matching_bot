package com.multi.matchingbot.member.domain.dto;

import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MemberRegisterDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @Size(min = 2, max = 15, message = "닉네임은 2~15자 이내여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,15}$", message = "닉네임은 한글, 영문, 숫자, '_', '-'만 사용할 수 있습니다.")
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "주소를 입력하세요.")
    private String addressRegion;
    private String addressDetail;

    @NotNull(message = "생년을 입력하세요.")
    private Integer year;
    @NotNull(message = "생월을 입력하세요.")
    private Integer month;
    @NotNull(message = "생일을 입력하세요.")
    private Integer day;

    @NotBlank(message = "성별을 선택하세요.")
    private String gender;
    private String phone1;
    private String phone2;
    private String phone3;

    @NotNull(message = "이용약관 동의는 필수입니다.")
    private Yn agreeService;

    @NotNull(message = "개인정보 처리방침 동의는 필수입니다.")
    private Yn agreePrivacy;

    private Yn agreeMarketing;
    private Yn agreeLocation;
    private Yn alertBookmark;
    private Yn alertResume;

    public boolean isLocationRequired() {
        return agreeLocation == Yn.Y || agreeLocation == Yn.N;
    }

    public boolean isMarketingEmail() {
        return agreeMarketing == Yn.Y || agreeMarketing == Yn.N;
    }


}