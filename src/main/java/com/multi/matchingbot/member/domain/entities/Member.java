package com.multi.matchingbot.member.domain.entities;

import com.multi.matchingbot.common.domain.entities.BaseEntity;
import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Member extends BaseEntity {

    // 해야할 일
    // validation 추가할 것
    // default 값 처리
    // 테이블 연관 관계 처리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;          // 역할

    @Column(nullable = false, unique = true)
    private String email;       // 이메일(로그인 아이디)

    @Column(nullable = false)
    private String name;        // 이름

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    @Column(nullable = false)
    private String address;     // 주소

    @Column(nullable = false)
    private String password;    // 비밀번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;        // 성별

    @Column(nullable = false)
    private LocalDate birth;// 생년월일

    @Column(nullable = false)
    private String phone;       // 전화 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeService;  // 서비스 동의

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreePrivacy;  // 정보 수집 동의

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn agreeLocation; // 위치 동의

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn alertBookmark; // 관심 기업 알림

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn alertResume;   // 이력서 알림

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn status;        // 가입 상태


}
