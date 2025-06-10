package com.multi.matchingbot.member.domain.dto;

import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileUpdateDto {

    private Long id;
    private Boolean agreeLocation;
    private Boolean agreeMarketing;
    private String email; // 읽기 전용

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 10, message = "이름은 2~10자 사이여야 합니다.")
    private String name;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,15}$", message = "닉네임은 2~15자의 한글, 영문, 숫자, '_', '-'만 사용할 수 있습니다.")
    @Column(unique = true, length = 15)
    private String nickname;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;
    private String addressDetail;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^(010|011|016|017|018|019)-?\\d{3,4}-?\\d{4}$", message = "유효한 휴대폰 번호 형식이어야 합니다.")
    private String phone;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @NotNull(message = "생년월일은 필수입니다.")
    @PastOrPresent(message = "생년월일은 현재 날짜 이전이어야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;


}