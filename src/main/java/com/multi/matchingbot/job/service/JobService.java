package com.multi.matchingbot.job.service;


import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.mapper.JobMapper;
import com.multi.matchingbot.job.repository.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository repository;
    private final GeoService geoService;

    public JobService(JobRepository repository, GeoService geoService) {
        this.repository = repository;
        this.geoService = geoService;
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

    public Job findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공고를 찾을 수 없습니다."));
    }

    @Transactional
    public void update(Long id, Job updatedJob) {
        Job job = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공고를 찾을 수 없습니다."));

        job.updateFrom(updatedJob); // 아래와 같이 정의 필요
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Job createJob(Job job) {
        // ✅ 주소가 있다면 위도, 경도 설정
        if (job.getAddress() != null && !job.getAddress().isBlank()) {
            double[] latLon = geoService.getLatLngFromAddress(job.getAddress());
            job.setLatitude(latLon[0]);
            job.setLongitude(latLon[1]);
        }

        return repository.save(job);
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