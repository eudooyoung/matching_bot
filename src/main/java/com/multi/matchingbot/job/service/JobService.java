package com.multi.matchingbot.job.service;


import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.repository.CompanyRepository;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.mapper.JobMapper;
import com.multi.matchingbot.job.repository.JobRepository;
import com.multi.matchingbot.job.repository.OccupationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository repository;
    private final CompanyRepository companyRepository;
    private final OccupationRepository occupationRepository;

    public JobService(JobRepository repository, CompanyRepository companyRepository, OccupationRepository occupationRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.occupationRepository = occupationRepository;
    }

    public Page<JobDto> getByCompanyIdPaged(Long companyId, Pageable pageable) {
        return repository.findByCompanyId(companyId, pageable)
                .map(JobMapper::toDto);
    }

    public List<JobDto> getAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public JobDto getById(Long id) {
        Job job = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공고를 찾을 수 없습니다."));
        return JobMapper.toDto(job);
    }

    @Transactional
    public JobDto save(JobDto dto) {
        if (dto.getOccupationId() == null) {
            throw new IllegalArgumentException("occupationId는 null일 수 없습니다.");
        }

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사입니다."));

        Occupation occupation = occupationRepository.findById(dto.getOccupationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직무입니다."));

        Job job = JobMapper.toEntity(dto, company, occupation);
        return JobMapper.toDto(repository.save(job));
    }

    @Transactional
    public JobDto update(Long id, JobDto dto) {
        Job job = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공고를 찾을 수 없습니다."));

        if (dto.getOccupationId() != null) {
            Occupation occupation = occupationRepository.findById(dto.getOccupationId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직무입니다."));
            job.setOccupation(occupation);
        }

        JobMapper.updateFromDto(job, dto);
        return JobMapper.toDto(repository.save(job));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<JobDto> getAllPaged(PageRequest pageable) {
        return repository.findAll(pageable).map(JobMapper::toDto);
    }

    private JobDto convertToDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyId(job.getCompany().getId())
                .occupationId(job.getOccupation() != null ? job.getOccupation().getId() : null)
                .build();
    }
}