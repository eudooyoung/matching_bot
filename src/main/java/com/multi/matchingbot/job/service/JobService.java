package com.multi.matchingbot.job.service;

import com.multi.matchingbot.job.JobRepository;
import com.multi.matchingbot.job.domain.Job;
import com.multi.matchingbot.job.domain.JobDto;
import com.multi.matchingbot.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private final JobRepository repository;

    public JobService(JobRepository repository) {
        this.repository = repository;
    }

    public List<JobDto> getAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public JobDto getById(Long id) {
        Job job = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공고 없음"));
        return JobMapper.toDto(job);
    }

    @Transactional
    public JobDto save(JobDto dto) {
        Job entity = JobMapper.toEntity(dto);
        Job saved = repository.save(entity);
        return JobMapper.toDto(saved);
    }

    public JobDto update(Long id, JobDto dto) {
        Job job = repository.findById(id).orElseThrow();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setAddress(dto.getAddress());
        job.setMainTask(dto.getMainTask());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setRequiredTraits(dto.getRequiredTraits());
        job.setSkillKeywords(dto.getSkillKeywords());
        job.setTraitKeywords(dto.getTraitKeywords());
        job.setStartDate(dto.getStartDate());
        job.setEndDate(dto.getEndDate());
        job.setEnrollEmail(dto.getEnrollEmail());
        job.setNotice(dto.getNotice());
        job.setUpdatedAt(LocalDateTime.now());
        job.setUpdatedBy("SYSTEM"); // 또는 로그인 사용자

        return JobMapper.toDto(repository.save(job));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // DTO ↔ Entity 변환 메서드
    private JobDto convertToDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyId(job.getCompany().getId())
                .build();
    }

    public Page<JobDto> getAllPaged(PageRequest pageable) {
        return repository.findAll(pageable).map(JobMapper::toDto);
    }
}