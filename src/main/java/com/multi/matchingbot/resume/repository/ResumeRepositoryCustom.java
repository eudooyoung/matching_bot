package com.multi.matchingbot.resume.repository;


import com.multi.matchingbot.resume.domain.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeRepositoryCustom {
    Page<Resume> searchWithFilters(String jobGroup, String jobType, String jobRole, String careerType, String companyName, Pageable pageable);
}
