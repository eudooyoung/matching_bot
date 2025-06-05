package com.multi.matchingbot.member.domain.dtos;

import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "주소를 입력하세요.")
    private String address;

    @NotNull(message = "생년을 입력하세요.")
    private Integer  year;
    @NotNull(message = "생월을 입력하세요.")
    private Integer  month;
    @NotNull(message = "생일을 입력하세요.")
    private Integer  day;

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



    /*public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isLocationRequired() {
        return locationRequired;
    }

    public void setLocationRequired(boolean locationRequired) {
        this.locationRequired = locationRequired;
    }

    public boolean isMarketingEmail() {
        return marketingEmail;
    }

    public void setMarketingEmail(boolean marketingEmail) {
        this.marketingEmail = marketingEmail;
    }

    public boolean isMarketingSms() {
        return marketingSms;
    }

    public void setMarketingSms(boolean marketingSms) {
        this.marketingSms = marketingSms;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public boolean isPrivacyRequired() {
        return privacyRequired;
    }

    public void setPrivacyRequired(boolean privacyRequired) {
        this.privacyRequired = privacyRequired;
    }

    public boolean isTermsRequired() {
        return termsRequired;
    }

    public void setTermsRequired(boolean termsRequired) {
        this.termsRequired = termsRequired;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }*/

}