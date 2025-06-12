package com.multi.matchingbot.job.service;

import com.multi.matchingbot.company.service.CompanyService;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository repository;
    private final GeoService geoService;
    private final JobRepository jobRepository;
    private final CompanyService companyService;

    public JobService(JobRepository repository, GeoService geoService, JobRepository jobRepository, CompanyService companyService) {
        this.repository = repository;
        this.geoService = geoService;
        this.jobRepository = jobRepository;
        this.companyService = companyService;
    }

    public List<Job> findAll() {
        return repository.findAll();
    }

    public Page<JobDto> getByCompanyIdPaged(Long companyId, Pageable pageable) {
        return repository.findByCompany_Id(companyId, pageable)
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

    @Transactional
    public void createJob(Job job) {
        if (job.getAddress() != null && !job.getAddress().isBlank()) {
            double[] latLon = geoService.getLatLngFromAddress(job.getAddress()); // geoService에서 위도, 경도 얻어옴
            job.setLatitude(latLon[0]);
            job.setLongitude(latLon[1]);
        }

        repository.save(job);
    }

    private JobDto convertToDto(Job job) {
        String companyName = companyService.findById(job.getCompanyId()).getName();

        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyId(job.getCompany().getId())
                .companyName(companyName)
                .address(job.getAddress())
                .occupationId(job.getOccupation() != null ? job.getOccupation().getId() : null)
                .build();
    }

    public Page<Job> getPageJobs(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Job> findByCompanyId(Long memberId) {
        return jobRepository.findByCompany_Id(memberId);
    }

    // 0609
    public List<JobDto> findByIdsPreserveOrder(List<Long> sortedIds) {
        List<Job> jobs = jobRepository.findAllById(sortedIds);
        Map<Long, Job> jobMap = jobs.stream().collect(Collectors.toMap(Job::getId, Function.identity()));

        return sortedIds.stream()
                .map(jobMap::get)
                .filter(Objects::nonNull)
                .map(JobMapper::toDto)
                .collect(Collectors.toList());
    }

}