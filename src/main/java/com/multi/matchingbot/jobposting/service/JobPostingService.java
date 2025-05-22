package com.multi.matchingbot.jobposting.service;

import com.multi.matchingbot.jobposting.domain.JobPosting;
import com.multi.matchingbot.jobposting.domain.JobPostingDto;
import com.multi.matchingbot.jobposting.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public List<JobPostingDto> getNearbyPostings(double lat, double lng, double radiusKm) {
        List<JobPosting> allJobs = jobPostingRepository.findAll();
        List<JobPostingDto> result = new ArrayList<>();

        for (JobPosting job : allJobs) {
            Double jobLat = job.getLatitude();
            Double jobLng = job.getLongitude();

            if (jobLat == null || jobLng == null) continue;

            double distance = calculateDistance(lat, lng, jobLat, jobLng);
            if (distance <= radiusKm) {
                result.add(JobPostingDto.fromEntity(job));
            }
        }
        return result;
    }

    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public List<com.multi.matchingbot.jobposting.model.dto.JobPostingDto> getAll() {
        return jobPostingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
