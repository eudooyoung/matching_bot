package com.multi.matchingbot.member.domain.dtos;

public class MemberRegisterDto {
    private String email;
    private String password;
    private String name;
    private String address;
    private int year;
    private int month;
    private int day;
    private String gender;
    private String phone1;
    private String phone2;
    private String phone3;

    private boolean termsRequired;
    private boolean privacyRequired;
    private boolean locationRequired;
    private boolean marketingEmail;
    private boolean marketingSms;

    public String getAddress() {
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
    }
}