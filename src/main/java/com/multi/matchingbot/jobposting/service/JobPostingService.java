package com.multi.matchingbot.jobposting.service;

import com.multi.matchingbot.jobposting.JobPosting;
import com.multi.matchingbot.jobposting.JobPostingRepository;
import com.multi.matchingbot.jobposting.model.dto.JobPostingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository repository;

    public List<JobPostingDto> getAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public JobPostingDto getById(Long id) {
        JobPosting job = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid job posting ID: " + id));
        return convertToDto(job);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // DTO ↔ Entity 변환 메서드
    private JobPostingDto convertToDto(JobPosting job) {
        return JobPostingDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyId(job.getCompany().getId())
                .build();
    }
}