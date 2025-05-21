package com.multi.matchingbot.jobposting;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job")
@Data
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;
    private Long occupationId;
    private String title;
    private String description;
    private String address;
    private String mainTask;
    private String requiredSkills;
    private String requiredTraits;
    private String skillKeywords;
    private String traitKeywords;
    private LocalDate startDate;
    private LocalDate endDate;
    private String enrollEmail;
    private String notice;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
