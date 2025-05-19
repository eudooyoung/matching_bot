package com.multi.matchingbot.auth.member;

import lombok.Data;

@Data
public class MemberDto {
    private Long memberCode;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    private String birthYear;
    private String birthMonth;
    private String birthDay;

    private String gender;
    private String address;
    private String phone;

    private boolean termsRequired;
    private boolean privacyRequired;
    private boolean locationRequired;
    private boolean marketingEmail;
    private boolean marketingSms;

    private String memberRole;
}
