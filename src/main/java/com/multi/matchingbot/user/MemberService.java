package com.multi.matchingbot.user;



import com.multi.matchingbot.common.domain.enums.Gender;
import com.multi.matchingbot.common.domain.enums.Role;
import com.multi.matchingbot.common.domain.enums.Yn;
import com.multi.matchingbot.user.domain.User;
import com.multi.matchingbot.user.domain.UserRegisterDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MemberService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .birth(LocalDate.of(dto.getYear(), dto.getMonth(), dto.getDay()))
                .gender(Gender.valueOf(dto.getGender())) // enum 처리
                .phone(dto.getPhone1() + "-" + dto.getPhone2() + "-" + dto.getPhone3())
                .agreeService(dto.isTermsRequired() ? Yn.Y : Yn.N)
                .agreePrivacy(dto.isPrivacyRequired() ? Yn.Y : Yn.N)
                .agreeLocation(dto.isLocationRequired() ? Yn.Y : Yn.N)
                .alertBookmark(dto.isMarketingEmail() ? Yn.Y : Yn.N)
                .alertResume(dto.isMarketingSms() ? Yn.Y : Yn.N)
                .role(Role.USER)
                .status(Yn.Y)
                .address(dto.getAddress())
                .build();

        userRepository.save(user);
    }
}