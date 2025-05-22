package com.multi.matchingbot.jobposting.controller;

import com.multi.matchingbot.jobposting.domain.JobPostingDto;
import com.multi.matchingbot.jobposting.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping("/nearby")
    public ResponseEntity<List<JobPostingDto>> getNearbyJobs(
            @RequestParam(name = "lat") double lat,
            @RequestParam(name = "lng") double lng,
            @RequestParam(name = "radiusKm", defaultValue = "5.0") double radiusKm) {

        List<JobPostingDto> jobs = jobPostingService.getNearbyPostings(lat, lng, radiusKm);
        return ResponseEntity.ok(jobs);
    }

}

