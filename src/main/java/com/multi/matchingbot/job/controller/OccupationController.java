package com.multi.matchingbot.job.controller;

import com.multi.matchingbot.job.domain.entity.Occupation;
import com.multi.matchingbot.job.repository.OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/occupations")
public class OccupationController {

    private final OccupationRepository occupationRepository;

    @GetMapping("/id")
    public ResponseEntity<?> getOccupationId(@RequestParam(value = "name", required = false) String jobRoleName) {
        Optional<Occupation> occupationOpt = occupationRepository.findByJobRoleName(jobRoleName);
        if (occupationOpt.isPresent()) {
            return ResponseEntity.ok(Map.of("id", occupationOpt.get().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "직무명을 찾을 수 없습니다."));
        }
    }
}
