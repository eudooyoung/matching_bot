package com.multi.matchingbot.member.domain.entity;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "회원 역할은 필수입니다.")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 10, message = "이름은 2~10자 사이여야 합니다.")
    @Column(nullable = false)
    private String name;

    @Column(length = 15, nullable = false, unique = true)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,15}$", message = "닉네임은 2~15자의 한글, 영문, 숫자, '_', '-'만 사용할 수 있습니다.")
    private String nickname;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    @NotBlank(message = "주소는 필수입니다.")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 하며, 영문/숫자/특수문자 조합을 권장합니다.")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "성별은 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NotNull(message = "생년월일은 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    @Column(nullable = false)
    private LocalDate birth;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^(010|011|016|017|018|019)-?\\d{3,4}-?\\d{4}$", message = "유효한 휴대폰 번호 형식이어야 합니다.")
    @Column(nullable = false)
    private String phone;

    @NotNull(message = "서비스 이용약관 동의는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreeService;

    @NotNull(message = "개인정보 수집 동의는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreePrivacy;

    @NotNull(message = "위치기반 서비스 동의는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn agreeLocation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeMarketing;


    @Enumerated(EnumType.STRING)
    @Column
    private Yn alertBookmark;


    @Enumerated(EnumType.STRING)
    @Column
    private Yn alertResume;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Yn status;
}