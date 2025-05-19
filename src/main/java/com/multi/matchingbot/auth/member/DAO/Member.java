package com.multi.matchingbot.auth.member.DAO;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

@Entity
@Table(name = "member")
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String phone;

    private boolean termsRequired;
    private boolean privacyRequired;
    private boolean locationRequired;
    private boolean marketingEmail;
    private boolean marketingSms;

    public Member() {}

    public Member(LocalDate birthDate, String email, String gender, Long id, boolean locationRequired, boolean marketingEmail, boolean marketingSms, String name, String password, String phone, boolean privacyRequired, boolean termsRequired) {
        this.birthDate = birthDate;
        this.email = email;
        this.gender = gender;
        this.id = id;
        this.locationRequired = locationRequired;
        this.marketingEmail = marketingEmail;
        this.marketingSms = marketingSms;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.privacyRequired = privacyRequired;
        this.termsRequired = termsRequired;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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


}
