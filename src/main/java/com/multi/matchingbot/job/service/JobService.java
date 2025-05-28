package com.multi.matchingbot.jobposting.service;

import com.multi.matchingbot.jobposting.JobPostingRepository;
import com.multi.matchingbot.jobposting.domain.JobPosting;
import com.multi.matchingbot.jobposting.domain.JobPostingDto;
import com.multi.matchingbot.jobposting.model.dao.JobPostingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostingService {

    @Autowired
    private final JobPostingRepository repository;

    public JobPostingService(JobPostingRepository repository) {
        this.repository = repository;
    }

    public List<JobPostingDto> getAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public JobPostingDto getById(Long id) {
        JobPosting job = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공고 없음"));
        return JobPostingMapper.toDto(job);
    }

    @Transactional
    public JobPostingDto save(JobPostingDto dto) {
        JobPosting entity = JobPostingMapper.toEntity(dto);
        JobPosting saved = repository.save(entity);
        return JobPostingMapper.toDto(saved);
    }

//    public JobPostingDto save(JobPostingDto dto) {
//        JobPosting entity = JobPostingMapper.toEntity(dto);
//
//        // 💡 영속 상태 Company 가져오기
//        Company company = companyRepository.findById(dto.getCompanyId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사 ID입니다."));
//        entity.setCompany(company);
//
//        entity.setCreatedAt(LocalDateTime.now());
//        entity.setCreatedBy("SYSTEM");
//
//        return JobPostingMapper.toDto(repository.save(entity));
//    }

    public JobPostingDto update(Long id, JobPostingDto dto) {
        JobPosting job = repository.findById(id).orElseThrow();
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

        return JobPostingMapper.toDto(repository.save(job));
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

    public Page<JobPostingDto> getAllPaged(PageRequest pageable) {
        return repository.findAll(pageable).map(JobPostingMapper::toDto);
    }
}