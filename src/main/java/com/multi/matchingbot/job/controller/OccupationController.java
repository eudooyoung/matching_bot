package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.domain.dto.JobGroupDto;
import com.multi.matchingbot.job.service.OccupationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/occupations")
public class OccupationController {

    private final OccupationService occupationService;

    @GetMapping("/hierarchy")
    public ResponseEntity<List<JobGroupDto>> getOccupationHierarchy() {
        return ResponseEntity.ok(occupationService.getOccupationHierarchy());
    }
}
